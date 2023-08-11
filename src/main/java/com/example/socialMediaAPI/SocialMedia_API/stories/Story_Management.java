package com.example.socialMediaAPI.SocialMedia_API.stories;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;


@Service
public class Story_Management {
	
	@Autowired
	StoryMongoRepository smr;

	
	public Date convert(ZonedDateTime source) {
		
		return Date.from(source.toInstant());
	}
	
	@Scheduled(fixedRate = 10000) // Specifies the method to be executed every 1000 milliseconds (1 second)
    public void constantlyRunningMethod() {
		
        // Your code logic goes here
		
		
		
		LocalDateTime ldt = LocalDateTime.now();
		ZoneId zoneId = ZoneId.of("UTC");
		ZonedDateTime zonedDateTime = ldt.atZone(zoneId);
		Date d = convert(zonedDateTime);
        smr.deleteByremoveDateLessThanEqual(d);
		
		
    }

}
