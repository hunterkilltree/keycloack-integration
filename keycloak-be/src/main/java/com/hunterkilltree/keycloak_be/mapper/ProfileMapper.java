package com.hunterkilltree.keycloak_be.mapper;

import org.mapstruct.Mapper;

import com.hunterkilltree.keycloak_be.dto.request.RegistrationRequest;
import com.hunterkilltree.keycloak_be.dto.response.ProfileResponse;
import com.hunterkilltree.keycloak_be.entity.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(RegistrationRequest request);

    ProfileResponse toProfileResponse(Profile profile);
}
