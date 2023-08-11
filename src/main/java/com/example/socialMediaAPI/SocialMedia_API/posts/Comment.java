package com.example.socialMediaAPI.SocialMedia_API.posts;

import java.time.LocalDate;
import java.util.HashSet;

import org.springframework.data.mongodb.core.mapping.Document;


import jakarta.persistence.Id;



@Document("Comments")
public class Comment {
	
	
	@Id
	private String id;
	
	
	private String postId;
	private String comment;
	private String userId;
	private HashSet<String> likes;
	private LocalDate postDate;
	
	public Comment() {
		
	}

	public Comment(String id, String postId, String comment, String userId, HashSet<String> likes, LocalDate postDate) {
		super();
		this.id = id;
		this.postId = postId;
		this.comment = comment;
		this.userId = userId;
		this.likes = likes;
		this.postDate = postDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public HashSet<String> getLikes() {
		return likes;
	}

	public void setLikes(HashSet<String> likes) {
		this.likes = likes;
	}

	public LocalDate getPostDate() {
		return postDate;
	}

	public void setPostDate(LocalDate postDate) {
		this.postDate = postDate;
	}
}
