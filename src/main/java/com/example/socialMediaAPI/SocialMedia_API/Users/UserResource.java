package com.example.socialMediaAPI.SocialMedia_API.Users;


import java.net.URI;
import java.time.LocalDate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.socialMediaAPI.SocialMedia_API.groups.Group;
import com.example.socialMediaAPI.SocialMedia_API.groups.GroupMongoRepository;
import com.example.socialMediaAPI.SocialMedia_API.posts.CommentMongoRepository;

import com.example.socialMediaAPI.SocialMedia_API.posts.PostMongoRepository;
import com.example.socialMediaAPI.SocialMedia_API.stories.StoryMongoRepository;


import jakarta.validation.Valid;

@RestController
public class UserResource {

	@Autowired
	UserCredJPAERepository UCJR;
	
	@Autowired
	UserDetMongoRepository UMR;
	
	@Autowired
	PostMongoRepository PMR;
	
	@Autowired
	GroupMongoRepository GMR;
	
	@Autowired
	CommentMongoRepository CMR;
	
	@Autowired
	StoryMongoRepository SMR;
	
	
	@Autowired
	private PasswordEncoder bpe;
	




	//Retrieving data from users
	

	

	
	@GetMapping("/users")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<UserDet> retrieveAllUsers(){
		return UMR.findAll();
	}
	
	
	
	@GetMapping("/userD/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public UserDet retrieveUser(@PathVariable String id) {
		return UMR.findById(id).get();
	}
	
	
	
	@GetMapping("/userC")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<UserCred> retrieveUserC(){
		return UCJR.findAll();
	}
	
	@GetMapping("/seeInfo")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public UserDet retrieveUserInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return UMR.findByusername(authentication.getName()).get();
	}
	
	@GetMapping("/seeInfo/{username}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public UserDet retrieveUserInfo(@PathVariable String username) {
		
		return UMR.findByusername(username).get();
	}
	
	
	
	
	
	
	
	
	
	

	
	
	
	@PostMapping("/user/{password}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> addUser(@PathVariable String password, @Valid @RequestBody UserDet userDet){
		
		if(!UMR.findByusername(userDet.getUsername()).isEmpty() || !UCJR.findByusername(userDet.getUsername()).isEmpty()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		
		userDet.setPosts(new LinkedList<String>());
		userDet.setStories(new LinkedList<String>());
		userDet.setFriends(new LinkedList<String>());
		userDet.setFriendRequests(new HashMap<String, String>());
		userDet.setFollowers(new HashSet<String>());
		userDet.setFollowing(new HashSet<String>());
		userDet.setGroupRequest(new HashSet<String>());
		userDet.setGroups(new HashSet<String>());
		userDet.setCreated(LocalDate.now());
		UserDet ud = UMR.save(userDet);
		UserCred uc = new UserCred(ud.getId(),ud.getUsername(), bpe.encode(password), "ROLE_USER");
		UCJR.save(uc);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/userD/{id}").buildAndExpand(ud.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	
	
	
	
	
	//Put Routes
	
	@PutMapping("/changeUsername/{username}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> changeUsername( @PathVariable String username) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<UserCred> uc = UCJR.findByusername(authentication.getName());
		
		if(ud.isEmpty() || uc.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(UMR.findByusername(username).isEmpty() && username.length() >= 4) {
			ud.get().setUsername(username);
			uc.get().setUsername(username);
			UMR.save(ud.get());
			UCJR.save(uc.get());
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
			
		}else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
		}
	}
	
	@PutMapping("/changeName")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> changeName(@Valid @RequestBody(required= false) String username){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		
		if(!ud.isEmpty()) {
			String name = username == null ? "" : username;
			ud.get().setName(name);
			UMR.save(ud.get());
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
			
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
	}
	
	
	@PutMapping("/changeBio")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> changeBio( @Valid @RequestBody(required= false) String bio){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		
		if(!ud.isEmpty()) {
			
			String bios = bio == null ? "" : bio;
			ud.get().setBio(bios);
			UMR.save(ud.get());
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
			
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
	}
	
	@PutMapping("/changePic")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> changePic(@Valid @RequestBody(required= false) String pic){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		
		if(!ud.isEmpty()) {
			
			String picture = pic == null ? "" : pic;
			ud.get().setProfilePic(picture);
			UMR.save(ud.get());
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
			
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
	}
	
	@PutMapping("/changeCountry")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> changeCountry(@Valid @RequestBody(required= false) String count){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		
		if(!ud.isEmpty()) {
			
			String country = count == null ? "" : count;
			ud.get().setCountry(country);
			UMR.save(ud.get());
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
			
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
	}
	
	@PutMapping("/changeCity")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> changeCity(@Valid @RequestBody(required= false) String cite){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		
		if(!ud.isEmpty()) {
			
			String city = cite == null ? "" : cite;
			ud.get().setCity(city);
			UMR.save(ud.get());
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
			
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
	}
	
	
	//End
	
	
	@PutMapping("/changePassword/{pass}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Object> changePassword(@PathVariable String pass){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserCred> uc = UCJR.findByusername(authentication.getName());
		if(uc.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		uc.get().setPassword(bpe.encode(pass));
		UCJR.save(uc.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
				
	}
	
	
	@DeleteMapping("/deleteUser")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Object> deleteUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<UserCred> uc = UCJR.findById(authentication.getName()) ;
		if(ud.isEmpty() && uc.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		
		for(String p : ud.get().getPosts()) {
			PMR.deleteById(p);
		}
		
		
		List<Group> groups = GMR.findAll();
		
		for(Group g : groups) {
			
			
			
			if(g.getMembers().contains(ud.get().getId())) {
				if(g.getGroupAdmin().equals(ud.get().getId())) {
					GMR.deleteById(g.getId());
					
				}else if(g.getGroupMods().contains(ud.get().getId())) {
					g.getGroupMods().remove(ud.get().getId());
				}
				
				g.getMembers().remove(ud.get().getId());
			}
			
		}
		
		
		UCJR.deleteById(ud.get().getId());
		UMR.deleteById(ud.get().getId());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	 
	 
}
