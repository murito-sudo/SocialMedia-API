package com.example.socialMediaAPI.SocialMedia_API.posts;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;


import com.example.socialMediaAPI.SocialMedia_API.Users.UserDetMongoRepository;


@RestController
public class CommentResource {
	
	@Autowired
	UserDetMongoRepository UMR;
	
	@Autowired
	PostMongoRepository PMR;
	
	@Autowired
	CommentMongoRepository CMR;
	
	
	@GetMapping("/comments")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public List<Comment> retrieveAllComments(){
		
		return CMR.findAll();
	}
	
	@GetMapping("/comment/{cid}/likes")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<String> retrieveComment(@PathVariable String cid){
		List<String> l = new LinkedList<String>();
		Optional<Comment> cd = CMR.findById(cid);
		
		for(String p : cd.get().getLikes()) {
			if(UMR.findById(p).isEmpty()) {
				cd.get().getLikes().remove(p);
			}else {
				l.add(UMR.findById(p).get().getUsername());
			}
		}
		
		CMR.save(cd.get());
		
		return l;
		
		
	}
	
	@GetMapping("/post/{id}/comments")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<Comment> retrievePostComments(@PathVariable String id){
		Optional<Post> pd = PMR.findById(id);
		List<Comment> l = new LinkedList<Comment>();
	
		if(pd.isEmpty()) {
			return null;
		}
		
		for(String p : pd.get().getComments()) {
			if(UMR.findById(CMR.findById(p).get().getUserId()).isEmpty()) {
				pd.get().getComments().remove(p);
			}else {
				l.add(CMR.findById(p).get());
			}
		}
		
		return l;
	}
	
	
	@GetMapping("/post/{id}/comment/{cid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public Comment retrieveComment(@PathVariable String id, @PathVariable String cid){
		Optional<Post> pd = PMR.findById(id);
	
		if(pd.isEmpty()) {
			return null;
		}
		
		for(String p : pd.get().getComments()) {
			if(p.equals(cid)) {
				Optional<Comment> cd = CMR.findById(cid);
				
				if(UMR.findById(cd.get().getId()).isEmpty()) {
					CMR.deleteById(cd.get().getId());
					break;
				}
				return CMR.findById(cid).get();
			}
		}
		
		return null;
	}
	
	
	


}
