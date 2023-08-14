package com.example.socialMediaAPI.SocialMedia_API.groups;

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
import com.example.socialMediaAPI.SocialMedia_API.posts.CommentMongoRepository;
import com.example.socialMediaAPI.SocialMedia_API.posts.Post;
import com.example.socialMediaAPI.SocialMedia_API.posts.PostMongoRepository;

import jakarta.validation.Valid;

@RestController
public class UserGroupResource {
	
	@Autowired
	GroupMongoRepository GMR;
	
	@Autowired
	UserDetMongoRepository UMR;
	
	@Autowired
	PostMongoRepository PMR;
	
	@Autowired
	CommentMongoRepository CMR;
	
	

	
	//Get Routes
	@GetMapping("/user/{id}/groups")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public List<Group> retrieveUserGroups(@PathVariable String id){
		List<Group> l = new LinkedList<Group>();
		
		Optional<UserDet> ud = UMR.findById(id);
		
		for(String p : ud.get().getGroups()) {
			
			if(GMR.findById(p).isEmpty()) {
				ud.get().getGroups().remove(p);
				
			}else {
				l.add(GMR.findById(p).get());
			}
			
			
			
		}
		
		UMR.save(ud.get());
		return l;
	}
	
	@GetMapping("/group/{gid}/posts")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public List<Post> retrieveGroupPosts( @PathVariable String id, @PathVariable String gid){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		List<Post> l = new LinkedList<Post>();
		
			Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
			Optional<Group> gd = GMR.findById(gid);
			
			if(ud.isEmpty() ||gd.isEmpty()) {
				return null;
			}
			
			
		
			
			for(String p : gd.get().getPosts()) {
				Optional<Post> pd = PMR.findById(p);
				
				if(pd.isEmpty() || UMR.findById(pd.get().getPosterId()).isEmpty()) {
				
					gd.get().getPosts().remove(p);
					if(UMR.findById(pd.get().getPosterId()).isEmpty()) {
						PMR.deleteById(p);
					}
					
				}else {
					l.add(pd.get());
				}
				
			}
		
			GMR.save(gd.get());
		
		
		return l;
		
	}

	
	@GetMapping("/group/{gid}/seeAdmin")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public UserDet seeGroupAdmin(@PathVariable String gid){		
		return UMR.findById(GMR.findById(gid).get().getGroupAdmin()).get();
	}
	
	@GetMapping("/group/{gid}/seeMods")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public List<UserDet> seeModMembers(@PathVariable String gid){
		List<UserDet> l = new LinkedList<UserDet>();
		Optional<Group> gd = GMR.findById(gid);
		
		if(gd.isEmpty()) {
			return null;
		}
		
	
		for(String s : gd.get().getGroupMods()) {
			if(UMR.findById(s).isEmpty()) {
				gd.get().getGroupMods().remove(s);
				GMR.save(gd.get());
			}else {
				l.add(UMR.findById(s).get());
			}
		}
		
		
		return l;
		
	}
	

	
	@GetMapping("/group/{gid}/seeMembers")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public List<UserDet> seeGroupMembers(@PathVariable String gid){
		List<UserDet> l = new LinkedList<UserDet>();
		Optional<Group> gd = GMR.findById(gid);
		
		if(gd.isEmpty()) {
			return null;
		}
		
	
		for(String s : gd.get().getMembers()) {
			if(UMR.findById(s).isEmpty()) {
				gd.get().getMembers().remove(s);
				GMR.save(gd.get());
			}else {
				l.add(UMR.findById(s).get());
			}
			
		}
		
		
		return l;
		
	}
	
	@GetMapping("/user/{id}/group/{gid}/seeBans")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public List<UserDet> seeUsersBan(@PathVariable String id, @PathVariable String gid){
		List<UserDet> l = new LinkedList<UserDet>();
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(id);
		Optional<Group> gd = GMR.findById(gid);
		
		if(ud.isEmpty() || gd.isEmpty()) {
			return null;
		}
		
		if(gd.get().getGroupAdmin().equals(ud.get().getId()) || gd.get().getGroupMods().contains(ud.get().getId())) {
			for(String s : gd.get().getBans()) {
				l.add(UMR.findById(s).get());
			}
		}
		
		return l;
		
	}
	

	
	//End
	
	
	
	@PostMapping("/post/{gid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> createPost(@PathVariable String gid, @Valid @RequestBody Post post){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
		Optional<Group> gd = GMR.findById(gid);
		
		if(ud.isEmpty() || gd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		
		if(!gd.get().getMembers().contains(ud.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		post.setPosterId(ud.get().getId());
		post.setPostedDate(LocalDate.now());
		post.setLikes(new HashSet<String>());
		post.setComments(new LinkedList<String>());
		
		
		
		PMR.save(post);
		gd.get().getPosts().add(post.getPostId());
		GMR.save(gd.get());
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/group/{id}/post/{pid}").buildAndExpand(gd.get().getId(), post.getPostId()).toUri();
		return ResponseEntity.created(location).build();
		
	}
	
	@DeleteMapping("group/{gid}/deleteGroupPost/{postid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> deleteUserPost(@PathVariable String gid, @PathVariable String postid){
			
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
			Optional<Group> gd = GMR.findById(gid);
			
			
			if(PMR.findById(postid).isEmpty() || ud.isEmpty() || gd.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			
			
			if(!PMR.findById(postid).get().getPosterId().equals(ud.get().getId())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
			for(String p : PMR.findById(postid).get().getComments()) {
				CMR.deleteById(p);
			}
			
		
			PMR.deleteById(postid);
		
			gd.get().getPosts().remove(postid);
			GMR.save(gd.get());
			
			
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	

	

	


	@PutMapping("/joinGroup/{gid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> userJoinGroup(@PathVariable String gid){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
		Optional<Group> gd = GMR.findById(gid);
		
		
		
		
		if(ud.isEmpty() || gd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(gd.get().getBans().contains(ud.get().getId()) || gd.get().getMembers().contains(ud.get().getId()) 
				|| gd.get().getGroupRequests().contains(ud.get().getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		
		if(gd.get().isPublic()) {
			ud.get().getGroups().add(gid);
			gd.get().getMembers().add(ud.get().getId());
		}else {
			ud.get().getGroupRequest().add(gid);
			gd.get().getGroupRequests().add(ud.get().getId());
		}
		
		UMR.save(ud.get());
		GMR.save(gd.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		
	}
	
	@PutMapping("/cancelGroupRequest/{gid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> userCancelRequestGroup(@PathVariable String gid){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
		Optional<Group> gd = GMR.findById(gid);
		
		
		
		
		if(ud.isEmpty() || gd.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		if(!gd.get().getGroupRequests().contains(ud.get().getId()) || !ud.get().getGroupRequest().contains(gid)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		ud.get().getGroupRequest().remove(gid);
		gd.get().getGroupRequests().remove(ud.get().getId());
		
		
		
		UMR.save(ud.get());
		GMR.save(gd.get());
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		
	}
	
	@PutMapping("/leaveGroup/{gid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseEntity<Object> userLeaveGroup(@PathVariable String gid){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<UserDet> ud = UMR.findByusernameIgnoreCase(authentication.getName());
		Optional<Group> gd = GMR.findById(gid);
		
		
		
			if(ud.isEmpty() || gd.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			
			
			if(!ud.get().getGroups().contains(gid)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
		
			
			ud.get().getGroups().remove(gid);
			gd.get().getMembers().remove(ud.get().getId());
			gd.get().getGroupMods().remove(ud.get().getId());
			
			
			if(gd.get().getGroupAdmin().equals(ud.get().getId())) {
				for(String p : gd.get().getMembers()) {
					Optional<UserDet> ud2 = UMR.findById(p);
					ud2.get().getGroups().remove(gid);
					UMR.save(ud2.get());
				}
				
				for(String p : gd.get().getPosts()) {
					PMR.deleteById(p);
					
				}
				
				GMR.delete(gd.get());
				
			}else {
				GMR.save(gd.get());
			}
			
			
			
			UMR.save(ud.get());
			
			
			
			
		
		
		
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	

	
	//End
	
}
