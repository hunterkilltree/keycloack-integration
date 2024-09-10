package com.hunterkilltree.keycloak_be.respository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hunterkilltree.keycloak_be.entity.Profile;

@Repository
public interface ProfileRepository extends MongoRepository<Profile, String> {}
