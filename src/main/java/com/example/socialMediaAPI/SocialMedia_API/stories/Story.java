package com.example.socialMediaAPI.SocialMedia_API.stories;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.springframework.data.mongodb.core.mapping.Document;



import jakarta.persistence.Id;


@Document("Stories")
public class Story {
	
	@Id
	private String id;
	private String posterId;
	private String imgUrl;
	private String text;
	private HashSet<String> views;
	private LocalDateTime postedDate;
	private LocalDateTime removeDate;
	
	public Story() {
		
	}
	
	

	public Story(String id, String posterId, String imgUrl, String text, HashSet<String> views,
			LocalDateTime postedDate, LocalDateTime removeDate) {
		super();
		this.id = id;
		this.posterId = posterId;
		this.imgUrl = imgUrl;
		this.text = text;
		this.views = views;
		this.postedDate = postedDate;
		this.removeDate = removeDate;
	}



	public String getId() {
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

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public HashSet<String> getViews() {
		return views;
	}

	public void setViews(HashSet<String> views) {
		this.views = views;
	}

	public LocalDateTime getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(LocalDateTime postedDate) {
		this.postedDate = postedDate;
	}

	public LocalDateTime getRemoveDate() {
		return removeDate;
	}

	public void setRemoveDate(LocalDateTime removeDate) {
		this.removeDate = removeDate;
	}
	
	
	
	
	
	
	
	
	

}
