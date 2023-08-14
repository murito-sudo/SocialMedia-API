package com.example.socialMediaAPI.SocialMedia_API.groups;


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
import com.example.socialMediaAPI.SocialMedia_API.posts.Post;
import com.example.socialMediaAPI.SocialMedia_API.posts.PostMongoRepository;


@RestController
public class GroupResource {
	
	@Autowired
	GroupMongoRepository GMR;
	
	@Autowired
	UserDetMongoRepository UMR;
	
	@Autowired
	PostMongoRepository PMR;
	
	
	

	@GetMapping("/groups")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Group> retrieveAllGroups(){
		return GMR.findAll();
	}
	
	@GetMapping("/group/{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public Group retrieveGroup(@PathVariable String id) {
		return GMR.findById(id).get();
	}
	
	@GetMapping("/group/{id}/posts")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<Post> retrieveGroupPosts(@PathVariable String id){
		
		Optional<Group> gd = GMR.findById(id);
		
		
		
		List<Post> l = new LinkedList<Post>();
		for(String d : gd.get().getPosts()) {
			Optional<Post> pd = PMR.findById(d);
			if(pd.isEmpty()) {
				gd.get().getPosts().remove(d);
				GMR.save(gd.get());
				continue;
			}
			
			if(UMR.findById(pd.get().getPosterId()).isEmpty()) {
				gd.get().getPosts().remove(d);
				PMR.delete(pd.get());
				GMR.save(gd.get());
				continue;
			}
			l.add(PMR.findById(d).get());
		}
		
		return l;
	}
	
	@GetMapping("/group/{id}/post/{pid}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public Post retrieveGroupPost(@PathVariable String id, @PathVariable String pid) {
		Optional<Group> gd = GMR.findById(id);
		Optional<Post> pd = PMR.findById(pid);
		
		if(gd.isEmpty() || pd.isEmpty()) {
			return null;
		}
		
		
		
		if(gd.get().getPosts().contains(pid)) {
			if(UMR.findById(pd.get().getPosterId()).isEmpty()) {
				gd.get().getPosts().remove(pid);
				GMR.save(gd.get());
				PMR.deleteById(pid);
			}else {
				return pd.get();
			}
			
		}
			
		
		return null;
	}
	
	@GetMapping("/group/{gid}/members")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<UserDet> getGroupMembers(@PathVariable String gid){
		Optional<Group> gd = GMR.findById(gid);
		List<UserDet> l = new LinkedList<UserDet>();
		
		for(String p : gd.get().getMembers()) {
			if(UMR.findById(p).isEmpty()) {
				gd.get().getMembers().remove(p);
				GMR.save(gd.get());
			}else {
				l.add(UMR.findById(p).get());
			}
		}
		
		return l;
	}
	
	
	@GetMapping("/group/{gid}/requests")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<UserDet> getGroupRequests(@PathVariable String gid){
		Optional<Group> gd = GMR.findById(gid);
		List<UserDet> l = new LinkedList<UserDet>();
		
		for(String p : gd.get().getGroupRequests()) {
			if(UMR.findById(p).isEmpty()) {
				gd.get().getGroupRequests().remove(p);
				GMR.save(gd.get());
			}else {
				l.add(UMR.findById(p).get());
			}
			
		}
		
		return l;
	}
	
	@GetMapping("/group/{gid}/bans")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<UserDet> getGroupBans(@PathVariable String gid){
		Optional<Group> gd = GMR.findById(gid);
		List<UserDet> l = new LinkedList<UserDet>();
		
		for(String p : gd.get().getBans()) {
			
			if(UMR.findById(p).isEmpty()) {
				gd.get().getBans().remove(p);
				GMR.save(gd.get());
			}else {
				l.add(UMR.findById(p).get());
			}
			
			
		}
		
		return l;
	}

}
