package com.example.socialMediaAPI.SocialMedia_API.stories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.socialMediaAPI.SocialMedia_API.Users.UserDet;
import com.example.socialMediaAPI.SocialMedia_API.Users.UserDetMongoRepository;

@RestController
public class StoryResource {

	@Autowired
	UserDetMongoRepository UMR;
	
	@Autowired
	StoryMongoRepository SMR;
	
	
	
	@GetMapping("/stories")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Story> retrieveStories() {
		return SMR.findAll();
	}
	
	@GetMapping("/story/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public Story retrieveStory(@PathVariable String id) {
		return SMR.findById(id).get();
	}
	
	
	@GetMapping("/story/{id}/views")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<String> retrieveUserViews(@PathVariable String id){
		Optional<Story> sd = SMR.findById(id);
		
		List<String> l = new LinkedList<String>();
		for(String p : sd.get().getViews()) {
			l.add(UMR.findById(p).get().getUsername());
		}
		
		return l;
		
		
	}
	
	
	
	@GetMapping("/user/{id}/stories")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<Story> retrieveStories(@PathVariable String id){
		List<Story> l = new LinkedList<Story>();
		Optional<UserDet> ud = UMR.findById(id);
		
		if(ud.isEmpty()) {
			return null;
		}
		for(String p : ud.get().getStories()) {
			l.add(SMR.findById(p).get());
		}
		
		return l;
	}
	
}
