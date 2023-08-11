package com.example.socialMediaAPI.SocialMedia_API.groups;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface GroupMongoRepository extends MongoRepository<Group, String>{
	@Query("{'posts': ?0, 'bans': ?0, 'groupRequests': ?0}")
	void deleteByValueInList(String value);
	


}
