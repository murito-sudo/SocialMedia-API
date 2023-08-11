package com.example.socialMediaAPI.SocialMedia_API.Users;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;




import jakarta.persistence.Id;

@Document("social-media-user")
public class UserDet {
	
	
	@Id
	private String id;

	private String username;
	private String name;
	private String bio;
	private String ProfilePic;
	private String country;
	private String city;
	private List<String> posts;
	private List<String> stories;
	private List<String> friends;
	private HashMap<String, String> friendRequests;
	private HashSet<String> followers;
	private HashSet<String> following;
	private HashSet<String> groupRequest;
	private HashSet<String> groups;
	private LocalDate created;
	
	public UserDet() {
		
	}

	public UserDet(String id, String username, String name, String bio, String profilePic, String country, String city,
			List<String> posts, List<String> stories, List<String> friends, HashMap<String, String> friendRequests,
			HashSet<String> followers, HashSet<String> following, HashSet<String> groupRequest, HashSet<String> groups,
			LocalDate created) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
		this.bio = bio;
		ProfilePic = profilePic;
		this.country = country;
		this.city = city;
		this.posts = posts;
		this.stories = stories;
		this.friends = friends;
		this.friendRequests = friendRequests;
		this.followers = followers;
		this.following = following;
		this.groupRequest = groupRequest;
		this.groups = groups;
		this.created = created;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getProfilePic() {
		return ProfilePic;
	}

	public void setProfilePic(String profilePic) {
		ProfilePic = profilePic;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<String> getPosts() {
		return posts;
	}

	public void setPosts(List<String> posts) {
		this.posts = posts;
	}

	public List<String> getStories() {
		return stories;
	}

	public void setStories(List<String> stories) {
		this.stories = stories;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public HashMap<String, String> getFriendRequests() {
		return friendRequests;
	}

	public void setFriendRequests(HashMap<String, String> friendRequests) {
		this.friendRequests = friendRequests;
	}

	public HashSet<String> getFollowers() {
		return followers;
	}

	public void setFollowers(HashSet<String> followers) {
		this.followers = followers;
	}

	public HashSet<String> getFollowing() {
		return following;
	}

	public void setFollowing(HashSet<String> following) {
		this.following = following;
	}

	public HashSet<String> getGroupRequest() {
		return groupRequest;
	}

	public void setGroupRequest(HashSet<String> groupRequest) {
		this.groupRequest = groupRequest;
	}

	public HashSet<String> getGroups() {
		return groups;
	}

	public void setGroups(HashSet<String> groups) {
		this.groups = groups;
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate created) {
		this.created = created;
	}
	
	
}
