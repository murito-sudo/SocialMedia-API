package com.example.socialMediaAPI.SocialMedia_API;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoDBConfiguration {
	@Autowired
    private Environment env;
	
	 @Bean
	    public MongoClient mongoClient() {
	        String mongoUri = env.getProperty("SPRING_DATA_MONGODB_URI");  // Replace with your MongoDB URI
	        
	        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
	                PojoCodecProvider.builder().automatic(true).build());
	        
	        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
	                MongoClientSettings.getDefaultCodecRegistry(),
	                pojoCodecRegistry);

	        MongoClientSettings settings = MongoClientSettings.builder()
	                .applyConnectionString(new ConnectionString(mongoUri))
	                .codecRegistry(codecRegistry)
	                .build();

	        return MongoClients.create(settings);
	    }
	
	

}
