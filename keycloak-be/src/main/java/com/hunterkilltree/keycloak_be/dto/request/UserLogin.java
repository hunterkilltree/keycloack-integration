package com.hunterkilltree.keycloak_be.dto.request;

import lombok.Data;

@Data
public class UserLogin {
    private String userName;

    private String password;
}
