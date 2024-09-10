package com.hunterkilltree.keycloak_be.respository;

import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hunterkilltree.keycloak_be.dto.identity.TokenExchangeParam;
import com.hunterkilltree.keycloak_be.dto.identity.TokenExchangeResponse;
import com.hunterkilltree.keycloak_be.dto.identity.UserCreationParam;

@FeignClient(name = "identity-client", url = "${idp.url}")
public interface IdentityClient {
  @PostMapping(value = "/realms/devteria/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  TokenExchangeResponse exchangeToken(@QueryMap TokenExchangeParam param);

  @PostMapping(value = "/admin/realms/devteria/users", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<?> createUser(
      @RequestHeader("authorization") String token,
      @RequestBody UserCreationParam param);
}
