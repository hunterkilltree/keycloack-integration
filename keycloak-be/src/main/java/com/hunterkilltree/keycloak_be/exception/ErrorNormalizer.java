package com.hunterkilltree.keycloak_be.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunterkilltree.keycloak_be.dto.identity.KeyCloakError;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ErrorNormalizer {
    // Serialize String to Object
    private final ObjectMapper objectMapper;

    // prevent if else
    private final Map<String, ErrorCode> errorMap;

    public ErrorNormalizer() {
        objectMapper = new ObjectMapper();
        errorMap = new HashMap<>();

        errorMap.put("User exists with same username", ErrorCode.USER_EXISTED);
        errorMap.put("User exists with same email", ErrorCode.EMAIL_EXISTED);
        errorMap.put("User name is missing", ErrorCode.USERNAME_IS_MISSING);
    }

    public AppException handleKeyCloakException(FeignException exception) {
        try {
            log.warn("Cannot complete request due to ", exception);
            var response = objectMapper.readValue(exception.contentUTF8(), KeyCloakError.class);
            if (Objects.nonNull(response.getErrorMessage())
                    && Objects.nonNull(errorMap.get(response.getErrorMessage()))) {
                return new AppException(errorMap.get(response.getErrorMessage()));
            }

        } catch (JsonProcessingException e) {
            log.error("Cannot deserialize content", e);
        }

        return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }
}
