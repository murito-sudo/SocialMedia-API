package com.example.socialMediaAPI.SocialMedia_API.stories;


import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface StoryMongoRepository extends MongoRepository<Story, String>{

	public void deleteAllByposterId(String id);
	List<Story> deleteByremoveDateLessThanEqual(Date referenceDateTime);
}
