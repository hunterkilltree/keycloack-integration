package com.hunterkilltree.keycloak_be.respository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hunterkilltree.keycloak_be.dto.identity.TokenExchangeParam;
import com.hunterkilltree.keycloak_be.dto.identity.TokenExchangeResponse;
import com.hunterkilltree.keycloak_be.dto.identity.UserCreationParam;
import com.hunterkilltree.keycloak_be.entity.Profile;

import feign.QueryMap;

@FeignClient(name = "identity-client", url = "${idp.url}")
public interface IdentityClient {

    @PostMapping(
            value = "/realms/${idp.realm}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeToken(@QueryMap TokenExchangeParam param);

    @PostMapping(value = "/admin/realms/${idp.realm}/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(@RequestHeader("authorization") String token, @RequestBody UserCreationParam param);

    @GetMapping(value = "/admin/realms/${idp.realm}/users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    Profile getByUserId(@RequestHeader("authorization") String token, @PathVariable String userId);
}
