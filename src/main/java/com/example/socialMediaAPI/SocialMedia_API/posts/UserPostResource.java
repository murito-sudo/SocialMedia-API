package com.example.socialMediaAPI.SocialMedia_API.posts;

import java.net.URI;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class UserPostResource {
	
	@Autowired
	UserDetMongoRepository UMR;
	
	@Autowired
	PostMongoRepository PMR;
	
	
	
	@GetMapping("/myPosts")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public List<Post> retrieveUserPosts(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		
		List<Post> l = new LinkedList<Post>();
		
		for(String p : ud.get().getPosts()) {
			l.add(PMR.findById(p).get());
		}
		
		return l;
		
	}
	
	@GetMapping("/post/{pid}/likes")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public List<UserDet> postlikes(@PathVariable String pid){
		
		
		List<UserDet> l = new LinkedList<UserDet>();
		
		Optional<Post> pd = PMR.findById(pid);
		
		for(String p : pd.get().getLikes()) {
			
			if(PMR.findById(p).isEmpty()) {
				pd.get().getLikes().remove(p);
			}else {
				l.add(UMR.findById(p).get());
			}
			
		}
		
		PMR.save(pd.get());
		
		return l;
		
	}
	
	@PostMapping("/createPost")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> createPost(@Valid @RequestBody Post post){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		
		if(ud.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(post.getPostContent() == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		post.setPosterId(ud.get().getId());
		post.setPostedDate(LocalDate.now());
		post.setLikes(new HashSet<String>());
		post.setComments(new LinkedList<String>());
		Post p = PMR.save(post);
		
		
		
		ud.get().getPosts().add(p.getPostId());
		UMR.save(ud.get());
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/post/{id}").buildAndExpand(p.getPostId()).toUri();
		return ResponseEntity.created(location).build();
	
	}
	
	
	
	@PutMapping("/post/{postid}/updatePost")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> updatePost(@PathVariable String postid, @Valid @RequestBody PostContent pc){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<Post> pd = PMR.findById(postid);
		
		if(ud.isEmpty() || pd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!pd.get().getPosterId().equals(ud.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		pd.get().setPostContent(pc);
		PMR.save(pd.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		
		
	}
	
	
	
	
	@PutMapping("/likesPost/{postid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> likePost(@PathVariable String postid){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<Post> pd = PMR.findById(postid);
		
		
		
		if(ud.isEmpty() || pd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(pd.get().getLikes().contains(ud.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		pd.get().getLikes().add(ud.get().getId());
		PMR.save(pd.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	
	@PutMapping("/dislikesPost/{postid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> unlikePost(@PathVariable String postid){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<Post> pd = PMR.findById(postid);
		
		if(ud.isEmpty() || pd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!pd.get().getLikes().contains(ud.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		pd.get().getLikes().remove(ud.get().getId());
		PMR.save(pd.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	
	
	@DeleteMapping("/deletePost/{id2}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> deleteUserPost(@PathVariable String postid){
			
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDet> ud = UMR.findByusername(authentication.getName());
			
			
			if(PMR.findById(postid).isEmpty() || ud.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			
			
			if(!ud.get().getPosts().contains(postid)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			PMR.deleteById(postid);
			ud.get().getPosts().remove(postid);
			UMR.save(ud.get());
			
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	

}
