package com.example.socialMediaAPI.SocialMedia_API.Users;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserCred {
	
	@Id
	private String id;
	
	
	boolean enabled;
	private String username;
	private String password;
	private String roles;
	
	
	public UserCred() {
		
	}


	public UserCred(String id, String username, String password,  String roles) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.roles = roles;
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


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}



	public String getRoles() {
		return roles;
	}


	public void setRoles(String roles) {
		this.roles = roles;
	}


	


	

	
	
	

}
