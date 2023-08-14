package com.example.socialMediaAPI.SocialMedia_API.stories;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.TimeZone;

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

import jakarta.validation.Valid;

@RestController
public class UserStoryResource {
	@Autowired
	UserDetMongoRepository UMR;
	
	@Autowired
	StoryMongoRepository SMR;
	
	@PostMapping("/createStory")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> createStory( @Valid @RequestBody Story story){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
		
		if(ud.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		story.setPosterId(ud.get().getId());
		
		story.setPostedDate(LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()));
		story.setRemoveDate(LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()).plusDays(1));
		
		story.setViews(new HashSet<String>());
		
		
		
		
		SMR.save(story);
		ud.get().getStories().add(story.getId());
		UMR.save(ud.get());
		
		
		
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/story/{id}").buildAndExpand(story.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	
	@PutMapping("/seeStory/{sid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> seeStory(@PathVariable String sid){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
		Optional<Story> sd = SMR.findById(sid);
		
		if(ud.isEmpty() || sd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!sd.get().getPosterId().equals(ud.get().getId())) {
			sd.get().getViews().add(ud.get().getId());
			SMR.save(sd.get());
			
		}
		
		
		
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		
		
	}
	
	
	@DeleteMapping("/deleteStory/{sid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> deleteStory( @PathVariable String sid){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
		Optional<Story> sd = SMR.findById(sid);
		
		if(ud.isEmpty() || sd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!sd.get().getPosterId().equals(ud.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		SMR.deleteById(sid);
		
		for(int x = 0; x < ud.get().getStories().size(); x++) {
			if(ud.get().getStories().get(x).equals(sid)) {
				ud.get().getStories().remove(x);
				break;
			}
			
		}
		UMR.save(ud.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		
	}
	

}
