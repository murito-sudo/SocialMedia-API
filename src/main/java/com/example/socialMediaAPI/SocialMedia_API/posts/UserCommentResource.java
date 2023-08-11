package com.example.socialMediaAPI.SocialMedia_API.posts;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashSet;
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

import jakarta.validation.Valid;


@RestController
public class UserCommentResource {
	@Autowired
	UserDetMongoRepository UMR;
	
	@Autowired
	PostMongoRepository PMR;
	
	@Autowired
	CommentMongoRepository CMR;
	
	
	@PostMapping("/post/{id}/addComment")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> addComment(@PathVariable String id, @Valid @RequestBody Comment comment){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Post> pd = PMR.findById(id);
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		if(pd.isEmpty() || ud.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		
		comment.setPostId(id);
		comment.setUserId(ud.get().getId());
		comment.setLikes(new HashSet<String>());
		comment.setPostDate(LocalDate.now());
		
		
		
		
		
		
		CMR.save(comment);
		pd.get().getComments().add(comment.getId());
		PMR.save(pd.get());
		
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.replacePath("/post/{id}/comment/{cid}").buildAndExpand(id, comment.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/editComment/{comid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> editComment(@PathVariable String comid, @Valid @RequestBody String text){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Comment> cd = CMR.findById(comid);
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		if(cd.isEmpty() || ud.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!cd.get().getUserId().equals(ud.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		cd.get().setComment(text);
		CMR.save(cd.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PutMapping("/likesComment/{comid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> likeComment(@PathVariable String comid){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<Comment> cd = CMR.findById(comid);
		
		if(ud.isEmpty() || cd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(cd.get().getLikes().contains(ud.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		cd.get().getLikes().add(ud.get().getId());
		
		CMR.save(cd.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PutMapping("/dislikesComment/{comid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> dislikeComment(@PathVariable String comid){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		Optional<Comment> cd = CMR.findById(comid);
		
		if(ud.isEmpty() || cd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!cd.get().getLikes().contains(ud.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		cd.get().getLikes().remove(ud.get().getId());
		CMR.save(cd.get());
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	
	@DeleteMapping("/deleteComment/{comid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> addComment( @PathVariable String comid){
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Comment> cd = CMR.findById(comid);
		Optional<UserDet> ud = UMR.findByusername(authentication.getName());
		if(cd.isEmpty() || ud.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		
		if(cd.get().getUserId().equals(ud.get().getId())) {
			Optional<Post> pd = PMR.findById(cd.get().getPostId());
			
			CMR.deleteById(comid);
			pd.get().getComments().remove(comid);
			PMR.save(pd.get());
			
		}else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		
		
		
	
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	

}
