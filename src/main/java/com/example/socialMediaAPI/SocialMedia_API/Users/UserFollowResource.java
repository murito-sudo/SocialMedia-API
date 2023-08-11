package com.example.socialMediaAPI.SocialMedia_API.Users;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserFollowResource {
	
	@Autowired
	UserDetMongoRepository UMR;
	
	@GetMapping("/user/{id}/followers")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public List<UserDet> retrieveFollowers(@PathVariable String id){
	
		Optional<UserDet> ud = UMR.findById(id);
		List<UserDet> l = new LinkedList<UserDet>();
		for(String x : ud.get().getFollowers()) {
			if(UMR.findById(x).isEmpty()) {
				ud.get().getFollowers().remove(x);
				
			}else {
				l.add(UMR.findById(x).get());
			}
			
		}
		
		UMR.save(ud.get());
		return l;
	}
	
	@GetMapping("/user/{id}/following")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public List<UserDet> retrieveFollowing(@PathVariable String id){
		
		Optional<UserDet> ud = UMR.findById(id);
		List<UserDet> l = new LinkedList<UserDet>();
		for(String x : ud.get().getFollowing()) {
			if(UMR.findById(x).isEmpty()) {
				ud.get().getFollowing().remove(x);
			}else {
				l.add(UMR.findById(x).get());
			}
			
		}
		
		UMR.save(ud.get());
		return l;
	}
	
	@PutMapping("/follow/{id2}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> followUser(@PathVariable String id2){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<UserDet> ud2 = UMR.findById(id2);
		
		if(ud.get().getId().equals(id2)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		if(ud.isEmpty() || ud2.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else {
			
			if(ud.get().getFollowing().contains(ud2.get().getId()) && 
					ud2.get().getFollowers().contains(ud.get().getId())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			ud.get().getFollowing().add(ud2.get().getId());
			ud2.get().getFollowers().add(ud.get().getId());
			
			UMR.save(ud.get());
			UMR.save(ud2.get());
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
			
		}
		
	}
	
	@PutMapping("/unfollow/{id2}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> unfollowUser(@PathVariable String id2) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<UserDet> ud2 = UMR.findById(id2);
		
		if(ud.isEmpty() || ud2.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		Predicate<? super String> predicate = user -> user.equals(ud2.get().getId());
		ud.get().getFollowing().removeIf(predicate);
		predicate = user -> user.equals(ud.get().getId());
		ud2.get().getFollowers().removeIf(predicate);
		
		UMR.save(ud.get());
		UMR.save(ud2.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		
	}
	

}
