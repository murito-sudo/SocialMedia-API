package com.example.socialMediaAPI.SocialMedia_API.posts;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostMongoRepository extends MongoRepository<Post, String> {
	public void deleteAllByposterId(String id);
}
