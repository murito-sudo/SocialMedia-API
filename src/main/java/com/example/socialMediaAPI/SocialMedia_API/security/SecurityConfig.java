package com.example.socialMediaAPI.SocialMedia_API.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	
	   
	   private final UserDetailService myUserDetailsService;

	    public SecurityConfig(UserDetailService myUserDetailsService) {
	        this.myUserDetailsService = myUserDetailsService;
	    }

	    @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        return http
	                .csrf().disable()
	                .authorizeRequests(auth -> auth
	                        .anyRequest().authenticated()
	                )
	                .userDetailsService(myUserDetailsService)
	                .headers(headers -> headers.frameOptions().sameOrigin())
	                .httpBasic(Customizer.withDefaults())
	                .build();
	    }
	    
	    @Bean
	    public Authentication getAuthentication() {
	        return SecurityContextHolder.getContext().getAuthentication();
	    }

	    @Bean
	    public PasswordEncoder encoder() {
	        return new BCryptPasswordEncoder();
	    }



}
