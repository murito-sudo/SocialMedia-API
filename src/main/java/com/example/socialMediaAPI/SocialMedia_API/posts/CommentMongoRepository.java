package com.example.socialMediaAPI.SocialMedia_API.posts;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentMongoRepository extends MongoRepository<Comment, String> {
	
	
	
}
