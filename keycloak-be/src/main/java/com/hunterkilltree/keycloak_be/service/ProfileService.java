package com.hunterkilltree.keycloak_be.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hunterkilltree.keycloak_be.dto.identity.Credential;
import com.hunterkilltree.keycloak_be.dto.identity.TokenExchangeParam;
import com.hunterkilltree.keycloak_be.dto.identity.UserCreationParam;
import com.hunterkilltree.keycloak_be.dto.request.RegistrationRequest;
import com.hunterkilltree.keycloak_be.dto.request.UserLogin;
import com.hunterkilltree.keycloak_be.dto.response.AccessToken;
import com.hunterkilltree.keycloak_be.dto.response.ProfileResponse;
import com.hunterkilltree.keycloak_be.entity.Profile;
import com.hunterkilltree.keycloak_be.exception.ErrorNormalizer;
import com.hunterkilltree.keycloak_be.mapper.ProfileMapper;
import com.hunterkilltree.keycloak_be.respository.IdentityClient;
import com.hunterkilltree.keycloak_be.respository.ProfileRepository;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileRepository profileRepository;
    ProfileMapper profileMapper;
    IdentityClient identityClient;
    ErrorNormalizer errorNormalizer;

    @Value("${idp.client-id}")
    @NonFinal
    String clientId;

    @Value("${idp.client-secret}")
    @NonFinal
    String clientSecret;

    public ProfileResponse getMyProfile() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        Optional<Profile> profileOptional = profileRepository.findByUserId(userId);

        if (profileOptional.isEmpty()) {
            // Store user profile to db if they sign up by social
            // get user from keycloak
            var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());

            log.info("TokenInfo {}", token);

            // Get user with client Token and save to db
            var newProfile = identityClient.getByUserId("Bearer " + token.getAccessToken(), userId);
            Profile profileSaved = profileRepository.save(newProfile);
            return profileMapper.toProfileResponse(profileSaved);
        }
        Profile profile = profileOptional.get();
        return profileMapper.toProfileResponse(profile);
    }

    public List<ProfileResponse> getAllProfiles() {
        var profiles = profileRepository.findAll();
        return profiles.stream().map(profileMapper::toProfileResponse).toList();
    }

    // Deprecation
    public AccessToken login(UserLogin user) {
        // Exchange client Token
        var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                .grant_type("password")
                .client_id("webapp_germany")
                .username(user.getUserName())
                .password(user.getPassword())
                .build());

        log.info("TokenInfo {}", token);
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(token.getAccessToken());
        return accessToken;
    }

    public ProfileResponse register(RegistrationRequest request) {
        try {
            // Create account in KeyCloak
            // Exchange client Token
            var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());

            log.info("TokenInfo {}", token);
            // Create user with client Token and given info

            // Get userId of keyCloak account
            var creationResponse = identityClient.createUser(
                    "Bearer " + token.getAccessToken(),
                    UserCreationParam.builder()
                            .username(request.getUsername())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .email(request.getEmail())
                            .enabled(true)
                            .emailVerified(false)
                            .credentials(List.of(Credential.builder()
                                    .type("password")
                                    .temporary(false)
                                    .value(request.getPassword())
                                    .build()))
                            .build());

            String userId = extractUserId(creationResponse);
            log.info("UserId {}", userId);

            var profile = profileMapper.toProfile(request);
            profile.setUserId(userId);

            profile = profileRepository.save(profile);

            return profileMapper.toProfileResponse(profile);

        } catch (FeignException exception) {
            throw errorNormalizer.handleKeyCloakException(exception);
        }
    }

    private String extractUserId(ResponseEntity<?> response) {
        String location = response.getHeaders().get("Location").getFirst();
        String[] splitedStr = location.split("/");
        return splitedStr[splitedStr.length - 1];
    }
}
