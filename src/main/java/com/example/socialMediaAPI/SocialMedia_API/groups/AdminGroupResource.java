package com.example.socialMediaAPI.SocialMedia_API.groups;

import java.net.URI;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.socialMediaAPI.SocialMedia_API.Users.UserDet;
import com.example.socialMediaAPI.SocialMedia_API.Users.UserDetMongoRepository;
import com.example.socialMediaAPI.SocialMedia_API.posts.PostMongoRepository;

import jakarta.validation.Valid;

@RestController
public class AdminGroupResource {
	
	@Autowired
	GroupMongoRepository GMR;
	
	@Autowired
	UserDetMongoRepository UMR;
	
	@Autowired
	PostMongoRepository PMR;
	
	@PostMapping("/createGroup")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Object> createGroup(@Valid @RequestBody Group group){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		
		if(ud.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		group.setGroupAdmin(ud.get().getId());
		group.setGroupMods(new HashSet<>());
		group.setMembers(new HashSet<String>());
		group.getMembers().add(ud.get().getId());
		group.setPosts(new LinkedList<String>());
		group.setGroupRequests(new LinkedList<String>());
		group.setBans(new HashSet<String>());
		
		
		GMR.save(group);
		ud.get().getGroups().add(group.getId());
		UMR.save(ud.get());
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/group/{id}").buildAndExpand(group.getId()).toUri();
		return ResponseEntity.created(location).build();
		
	}
	
	@PutMapping("/group/{gid}/changeName")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Object> changeGroupName( @PathVariable String gid, @Valid @RequestBody String name){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<Group> gd = GMR.findById(gid);
		
		if(gd.isEmpty() || ud.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!gd.get().getGroupAdmin().equals(ud.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		gd.get().setGroupName(name);
		GMR.save(gd.get());
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PutMapping("/group/{gid}/addMod/{id2}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Object> addMod(@PathVariable String gid, @PathVariable String id2){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Group> gd = GMR.findById(gid);
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<UserDet> ud2 = UMR.findById(id2);
		
		if(gd.isEmpty() || ud.isEmpty() || ud2.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!gd.get().getMembers().contains(id2) || !gd.get().getGroupAdmin().equals(ud.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		gd.get().getGroupMods().add(id2);
		GMR.save(gd.get());
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	
	
	@PutMapping("/acceptRequest/{id2}/group/{gid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Object> acceptRequest(@PathVariable String id2, @PathVariable String gid){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<UserDet> ud2 = UMR.findById(id2);
		Optional<Group> gd = GMR.findById(gid);
		
		if(ud.isEmpty() || ud2.isEmpty() || gd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!gd.get().getGroupRequests().contains(id2) || !ud2.get().getGroupRequest().contains(gid)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(ud2.get().getGroups().contains(gid)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		if(gd.get().getBans().contains(id2) || (!gd.get().getGroupAdmin().equals(ud.get().getId()) 
				&& !gd.get().getGroupMods().contains(ud.get().getId()))) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		gd.get().getGroupRequests().remove(id2);
		gd.get().getMembers().add(id2);
		ud2.get().getGroupRequest().remove(gid);
		ud2.get().getGroups().add(gid);
		
		UMR.save(ud2.get());
		GMR.save(gd.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		
		
	}
	
	@PutMapping("/declineRequest/{id2}/group/{gid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Object> declineRequest(@PathVariable String id2, @PathVariable String gid){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<UserDet> ud2 = UMR.findById(id2);
		Optional<Group> gd = GMR.findById(gid);
		
		if(ud.isEmpty() || ud2.isEmpty() || gd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!gd.get().getGroupRequests().contains(id2)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(gd.get().getBans().contains(id2) || (!gd.get().getGroupAdmin().equals(ud.get().getId()) 
				&& !gd.get().getGroupMods().contains(ud.get().getId()))) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		gd.get().getGroupRequests().remove(id2);
		ud2.get().getGroupRequest().remove(gid);
		
		GMR.save(gd.get());
		UMR.save(ud2.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		
		
	}
	
	@PutMapping("/bans/{id2}/fromGroup/{gid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Object> banUser(@PathVariable String id2, @PathVariable String gid){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<UserDet> ud2 = UMR.findById(id2);
		Optional<Group> gd = GMR.findById(gid);
		
		
		if(ud.isEmpty() || ud2.isEmpty() || gd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(gd.get().getGroupAdmin().equals(id2)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}else if(gd.get().getGroupMods().contains(id2)) {
			if(gd.get().getGroupAdmin().equals(ud.get().getId())) {
				gd.get().getGroupMods().remove(id2);
				gd.get().getMembers().remove(id2);
				gd.get().getBans().add(id2);
				ud2.get().getGroups().remove(gid);
			}else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
		}else if(gd.get().getMembers().contains(id2)) {
			if(gd.get().getGroupAdmin().equals(ud.get().getId()) || gd.get().getGroupMods().contains(ud.get().getId())) {
				gd.get().getGroupMods().remove(id2);
				gd.get().getMembers().remove(id2);
				gd.get().getBans().add(id2);
				ud2.get().getGroups().remove(gid);
				
			}else {
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			}
			
		}else {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			
		}
		
		GMR.save(gd.get());
		UMR.save(ud2.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		

	}
	
	@PutMapping("/unbans/{id2}/fromGroup/{gid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Object> unbanUser(@PathVariable String id2, @PathVariable String gid){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<UserDet> ud2 = UMR.findById(id2);
		Optional<Group> gd = GMR.findById(gid);
		
		if(ud.isEmpty() || ud2.isEmpty() || gd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if((gd.get().getGroupMods().contains(ud.get().getId()) || gd.get().getGroupAdmin().equals(ud.get().getId())) 
				&& gd.get().getBans().contains(id2)){
			gd.get().getBans().remove(id2);
			GMR.save(gd.get());
			
		}else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	//Delete Routes
	
	@DeleteMapping("/deleteGroup/{gid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public ResponseEntity<Object> deleteGroup(@PathVariable String gid){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<Group> gd = GMR.findById(gid);
		
		if(ud.isEmpty() || gd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!gd.get().getGroupAdmin().equals(ud.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		for(String p : gd.get().getMembers()) {
			Optional<UserDet> ud2 = UMR.findById(p);
			ud2.get().getGroups().remove(gid);
			UMR.save(ud2.get());
			
		}
		
		for(String p : gd.get().getGroupRequests()) {
			Optional<UserDet> ud2 = UMR.findById(p);
			ud2.get().getGroupRequest().remove(gid);
			UMR.save(ud2.get());
		}
		
		
		GMR.deleteById(gid);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		
	}
	
	//End

}
