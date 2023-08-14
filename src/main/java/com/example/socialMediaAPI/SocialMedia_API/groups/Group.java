package com.example.socialMediaAPI.SocialMedia_API.groups;

import java.util.HashSet;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;

@Document("Groups")
public class Group {
	
	@Id
	private String id;
	private String groupAdmin;
	private String groupName;
	private boolean isPublic;
	
	private HashSet<String> groupMods;
	private HashSet<String> members;
	private List<String> posts;
	
	private HashSet<String> groupRequests;
	private HashSet<String> bans;
	
	public Group() {
		
	}

	public Group(String id, String groupAdmin, String groupName, boolean isPublic, HashSet<String> groupMods,
			HashSet<String> members, List<String> posts, HashSet<String> groupRequests, HashSet<String> bans) {
		super();
		this.id = id;
		this.groupAdmin = groupAdmin;
		this.groupName = groupName;
		this.isPublic = isPublic;
		this.groupMods = groupMods;
		this.members = members;
		this.posts = posts;
		this.groupRequests = groupRequests;
		this.bans = bans;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupAdmin() {
		return groupAdmin;
	}

	public void setGroupAdmin(String groupAdmin) {
		this.groupAdmin = groupAdmin;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public HashSet<String> getGroupMods() {
		return groupMods;
	}

	public void setGroupMods(HashSet<String> groupMods) {
		this.groupMods = groupMods;
	}

	public HashSet<String> getMembers() {
		return members;
	}

	public void setMembers(HashSet<String> members) {
		this.members = members;
	}

	public List<String> getPosts() {
		return posts;
	}

	public void setPosts(List<String> posts) {
		this.posts = posts;
	}

	public HashSet<String> getGroupRequests() {
		return groupRequests;
	}

	public void setGroupRequests(HashSet<String> groupRequests) {
		this.groupRequests = groupRequests;
	}

	public HashSet<String> getBans() {
		return bans;
	}

	public void setBans(HashSet<String> bans) {
		this.bans = bans;
	}
}
