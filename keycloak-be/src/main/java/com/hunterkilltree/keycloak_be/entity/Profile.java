package com.hunterkilltree.keycloak_be.entity;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("profile")
public class Profile {
    @MongoId
    String profileId;
    // UserId from keycloak
    @JsonProperty("id")
    String userId;

    String email;
    String username;
    String firstName;
    String lastName;
    LocalDate dob;
}
