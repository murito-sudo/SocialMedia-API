package com.example.socialMediaAPI.SocialMedia_API.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	    private String password;
	    private String authority;

	    public CustomUserDetails(String username, String password, String authority) {
	        this.username = username;
	        this.password = password;
	        this.authority = authority;
	    }

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return List.of(new SimpleGrantedAuthority(authority));
	    }

	    @Override
	    public String getPassword() {
	        return password;
	    }



	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

    // Other UserDetails methods (accountNonExpired, accountNonLocked, credentialsNonExpired, isEnabled)
    // ... Implement them based on your requirements
}
