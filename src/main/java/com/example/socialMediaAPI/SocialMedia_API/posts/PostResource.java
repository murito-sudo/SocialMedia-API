package com.example.socialMediaAPI.SocialMedia_API.posts;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	@PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
	public Post retrievePost(@PathVariable String id) {
		return pmr.findById(id).get();
	}
	
	@GetMapping("/user/{id}/posts")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public List<Post> retrieveUserPosts(@PathVariable String id){
		
		List<Post> l = new LinkedList<Post>();
		
		Optional<UserDet> ud = umr.findById(id);
		
		for(String p : ud.get().getPosts()) {
			l.add(pmr.findById(p).get());
		}
		
		return l;
	}
	
	@GetMapping("/user/{id}/post/{postid}")
	@PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
	public Post retrieveUserPosts(@PathVariable String id, @PathVariable String postid){
		
		
		Optional<UserDet> ud = umr.findById(id);
	
		
		if(!ud.get().getPosts().contains(postid) || ud.isEmpty()) {
			return null;
		}
		
		return pmr.findById(postid).get();
	}
	
	@GetMapping("/post/{id}/likes")
	@PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<UserDet> retrievePostLikes(@PathVariable String id){
		
		Optional<Post> pd = pmr.findById(id);
		List<UserDet> l = new LinkedList<UserDet>();
		
		for(String d : pd.get().getLikes()) {
			l.add(umr.findById(d).get());
		}
	
		
		return l;
	}
	
	
	
	@GetMapping("/post/{id}/username/likes")
	@PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<String> retrievePostLikesUsernames(@PathVariable String id){
		
		Optional<Post> pd = pmr.findById(id);
		List<String> l = new LinkedList<String>();
		
		for(String d : pd.get().getLikes()) {
			l.add(umr.findById(d).get().getUsername());
		}
		
		return l;
		
	}
	
	
	
	
	
	
	
	@DeleteMapping("/deletePost/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Object> deleteId(@PathVariable String id){
		if(pmr.findById(id).isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		Optional<Post> pd = pmr.findById(id);
		
		Optional<UserDet> ud = umr.findById(pd.get().getPosterId());
		
		ud.get().getPosts().remove(id);
		
		
		pmr.deleteById(id);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		
		
		
	}
	
}
