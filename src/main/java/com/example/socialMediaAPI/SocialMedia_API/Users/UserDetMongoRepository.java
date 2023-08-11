package com.example.socialMediaAPI.SocialMedia_API.Users;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDetMongoRepository extends MongoRepository<UserDet, String> {
	public Optional<UserDet> findByusername(String username);

}
