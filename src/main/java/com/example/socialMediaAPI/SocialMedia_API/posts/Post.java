package com.example.socialMediaAPI.SocialMedia_API.posts;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;




import jakarta.persistence.Id;

@Document("Posts")
public class Post {
	
	@Id
	private String id;
	
	private String posterId;
	private PostContent postContent;
	private HashSet<String> likes;
	private List<String> comments;
	private LocalDate postedDate;
	
	public Post() {
		
	}

	public Post(String id, String posterId, PostContent postContent, HashSet<String> likes,
			List<String> comments, LocalDate postedDate) {
		super();
		this.id = id;
		
		this.posterId = posterId;
		this.postContent = postContent;
		this.likes = likes;
		this.comments = comments;
		this.postedDate = postedDate;
	}

	public String getPostId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public String getPosterId() {
		return posterId;
	}

	public void setPosterId(String posterId) {
		this.posterId = posterId;
	}

	public PostContent getPostContent() {
		return postContent;
	}

	public void setPostContent(PostContent postContent) {
		this.postContent = postContent;
	}

	public HashSet<String> getLikes() {
		return likes;
	}

	public void setLikes(HashSet<String> likes) {
		this.likes = likes;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	public LocalDate getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(LocalDate postedDate) {
		this.postedDate = postedDate;
	}
	
	
	
	
	

}
