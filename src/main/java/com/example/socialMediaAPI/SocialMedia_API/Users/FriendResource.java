package com.example.socialMediaAPI.SocialMedia_API.Users;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
public class FriendResource {
	
	@Autowired
	UserDetMongoRepository UMR;
	
	
	//Retrieve Data
	
	@GetMapping("/user/{id}/friends")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public List<UserDet> retrieveFriends(@PathVariable String id){
		Optional<UserDet> ud = UMR.findById(id);
		List<UserDet> l = new LinkedList<UserDet>();
		
		for(String f : ud.get().getFriends()) {
			if(UMR.findById(f).isEmpty()) {
				ud.get().getFriends().remove(f);
			}else {
				l.add(UMR.findById(f).get());
			}
			
		}
		
		UMR.save(ud.get());
		
		return l;
	}
	
	@GetMapping("/fRequests")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public List<UserDet> retrieveRequests(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
		List<UserDet> l = new LinkedList<UserDet>();
	
		
		for(String f : ud.get().getFriendRequests().keySet()) {
			
			if(UMR.findById(f).isEmpty()) {
				ud.get().getFriendRequests().remove(f);
			}else {
				l.add(UMR.findById(f).get());
			}
		}
		
		UMR.save(ud.get());
		
		return l;
	}
	
	//End
	
	@PutMapping("/sendFriendR/{id2}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> sendFriendRequest(@PathVariable String id2){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
		Optional<UserDet> ud2 = UMR.findById(id2);
		
		if(ud.get().getId().equals(id2)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		if(ud.isEmpty() || ud2.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else {
			
			if(ud.get().getFriendRequests().containsKey(id2) || ud2.get().getFriendRequests().containsKey(ud.get().getId())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
			ud.get().getFriendRequests().put(id2, "Sender");
			ud2.get().getFriendRequests().put(ud.get().getId(), "Reciever");
			
			UMR.save(ud.get());
			UMR.save(ud2.get());
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
			
		}
	}

	
	
	
	@PutMapping("/acceptRequestF/{id2}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> acceptRequest(@PathVariable String id2){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
		Optional<UserDet> ud2 = UMR.findById(id2);
		if(ud.get().getId().equals(id2)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		if(ud.isEmpty() || ud2.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else {
			
			if((ud.get().getFriendRequests().containsKey(id2) 
					&& ud.get().getFriendRequests().get(id2).equals("Reciever")) 
					&& ud2.get().getFriendRequests().containsKey(ud.get().getId()) 
					&& ud2.get().getFriendRequests().get(ud.get().getId()).equals("Sender")) {
				ud.get().getFriendRequests().remove(id2);
				ud2.get().getFriendRequests().remove(ud.get().getId());
				
				ud.get().getFriends().add(id2);
				ud2.get().getFriends().add(ud.get().getId());
				UMR.save(ud.get());
				UMR.save(ud2.get());
				
			}else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
			
		}
		
		
	}
	
	@PutMapping("/declineRequestF/{id2}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> declineRequest(@PathVariable String id2){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
		Optional<UserDet> ud2 = UMR.findById(id2);
		if(ud.get().getId().equals(id2)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		if(ud.isEmpty() || ud2.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else {
			
			if(ud.get().getFriendRequests().containsKey(id2) && ud2.get().getFriendRequests().containsKey(ud.get().getId())) {
				
				ud.get().getFriendRequests().remove(id2);
				ud2.get().getFriendRequests().remove(ud.get().getId());
			
				UMR.save(ud.get());
				UMR.save(ud2.get());
				
			}else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
			
		}
		
	}
	
	@PutMapping("/unfriend/{id2}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> unfriendUser(@PathVariable String id2){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
		Optional<UserDet> ud2 = UMR.findById(id2);
		if(ud.get().getId().equals(id2)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		if(ud.isEmpty() || ud2.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else {
			
			if(ud.get().getFriends().contains(id2) && ud2.get().getFriends().contains(ud.get().getId())) {
				
				ud.get().getFriends().remove(id2);
				ud2.get().getFriends().remove(ud.get().getId());
			
				UMR.save(ud.get());
				UMR.save(ud2.get());
				
			}else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
			
		}
		
	}


}
