package com.example.socialMediaAPI.SocialMedia_API.Users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredJPAERepository extends JpaRepository<UserCred, String> {
	public Optional<UserCred> findByusername(String username);
	public void deleteByusername(String username);
}
