package com.example.socialMediaAPI.SocialMedia_API;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.socialMediaAPI.SocialMedia_API.Users.UserCred;
import com.example.socialMediaAPI.SocialMedia_API.Users.UserCredJPAERepository;
import com.example.socialMediaAPI.SocialMedia_API.stories.StoryMongoRepository;


@SpringBootApplication
@EnableScheduling
public class SocialMediaApiApplication {

	@Autowired
	private PasswordEncoder bpe;
	
	
	
	@Autowired
    private Environment env;
	
	
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(SocialMediaApiApplication.class, args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("MongoDB URI: " + env.getProperty("SPRING_DATA_MONGODB_URI"));
    }
	
	
	@Bean
	CommandLineRunner commandLineRunner(UserCredJPAERepository ucjr, StoryMongoRepository smr) {
		
		
		
		return args -> {
			
			ucjr.save(new UserCred("0", "Morenight", bpe.encode("Abreu"),"ROLE_ADMIN"));
			
			
		};
	}
	
	

}
