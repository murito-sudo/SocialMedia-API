package com.example.socialMediaAPI.SocialMedia_API.posts;


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
public class PostResource {
	
	@Autowired
	PostMongoRepository pmr;
	
	@Autowired
	UserDetMongoRepository umr;
	
	@Autowired
	CommentMongoRepository cmr;
	
	
	
	
	
	

	@GetMapping("/posts")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Post> retrievePosts() {
		return pmr.findAll();
	}
	
	@GetMapping("/post/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public Post retrievePost(@PathVariable String id) {
		Optional<Post> pd = pmr.findById(id);
		if(pd.isEmpty()) {
			return null;
		}
		if(umr.findById(pd.get().getPosterId()).isEmpty()) {
			pmr.deleteById(id);
			return null;
			
		}
		return pmr.findById(id).get();
	}
	
	@GetMapping("/user/{id}/posts")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<Post> retrieveUserPosts(@PathVariable String id){
		
		List<Post> l = new LinkedList<Post>();
		
		Optional<UserDet> ud = umr.findById(id);
		
		for(String p : ud.get().getPosts()) {
			
			l.add(pmr.findById(p).get());
		}
		
		return l;
	}
	
	@GetMapping("/user/{id}/post/{postid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public Post retrieveUserPosts(@PathVariable String id, @PathVariable String postid){
		
		
		Optional<UserDet> ud = umr.findById(id);
	
		
		if(!ud.get().getPosts().contains(postid) || ud.isEmpty()) {
			return null;
		}
		
		return pmr.findById(postid).get();
	}
	
	@GetMapping("/post/{pid}/likes")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public List<UserDet> postlikes(@PathVariable String pid){
		
		
		List<UserDet> l = new LinkedList<UserDet>();
		
		Optional<Post> pd = pmr.findById(pid);
		
		for(String p : pd.get().getLikes()) {
			
			if(umr.findById(p).isEmpty()) {
				pd.get().getLikes().remove(p);
			}else {
				l.add(umr.findById(p).get());
			}
			
		}
		
		pmr.save(pd.get());
		
		return l;
		
	}
	
	
	
	@GetMapping("/post/{id}/username/likes")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<String> retrievePostLikesUsernames(@PathVariable String id){
		
		Optional<Post> pd = pmr.findById(id);
		List<String> l = new LinkedList<String>();
		
		for(String d : pd.get().getLikes()) {
			l.add(umr.findById(d).get().getUsername());
		}
		
		return l;
		
	}
	
	
	
}
