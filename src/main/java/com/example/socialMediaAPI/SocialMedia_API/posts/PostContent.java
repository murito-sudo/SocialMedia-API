package com.example.socialMediaAPI.SocialMedia_API.posts;

import java.util.List;

public class PostContent {

	private List<String> imageurl;
	private String text;
	private List<String> tags;
	
	public PostContent() {
		
	}
	
	
	public PostContent(List<String> imageurl, String text, List<String> tags) {
		super();
		this.imageurl = imageurl;
		this.text = text;
		this.tags = tags;
	}
	public List<String> getImageurl() {
		return imageurl;
	}
	public void setImageurl(List<String> imageurl) {
		this.imageurl = imageurl;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	

}
