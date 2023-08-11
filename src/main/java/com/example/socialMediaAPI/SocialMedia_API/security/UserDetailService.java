package com.example.socialMediaAPI.SocialMedia_API.security;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.AuthorityUtils;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.socialMediaAPI.SocialMedia_API.Users.UserCred;
import com.example.socialMediaAPI.SocialMedia_API.Users.UserCredJPAERepository;

@Service
public class UserDetailService implements UserDetailsService {
	@Autowired
	private UserCredJPAERepository UCJR;
	
	public UserDetailService() {
		
	}
	
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserCred user = UCJR.findByusername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
		
	    return new User(user.getUsername(), user.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
	}
	


}
