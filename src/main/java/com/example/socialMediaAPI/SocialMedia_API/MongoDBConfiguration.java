package com.example.socialMediaAPI.SocialMedia_API;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoDBConfiguration  {

		
    	private Environment env;
	
		@Autowired
	    public MongoDBConfiguration(Environment env) {
	        this.env = env;
	    }

		
	    @Bean
	    public MongoClient mongoClient() {
	        String mongoUri = env.getProperty("SPRING_DATA_MONGODB_URI") == null ? "mongodb://localhost:27017/social-media-api" : env.getProperty("SPRING_DATA_MONGODB_URI"); // Replace with your MongoDB URI
		
	        
	        System.out.println("This is: " + mongoUri);
	        
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
	    
	    @EventListener(ApplicationReadyEvent.class)
	    public void performMongoDBConfiguration() {
	        // Here you can add additional MongoDB-specific configuration
	        // after the application is fully initialized.
	        // For example, you might want to set indexes or other options.

	        // Access the mongoClient bean you've defined above
	        MongoClient mongoClient = mongoClient();

	        // You can use the mongoClient to perform configuration tasks
	        // on your MongoDB instance as needed.
	        // For example:
	        // mongoClient.getDatabase("your-database-name")
	        //     .getCollection("your-collection-name")
	        //     .createIndex(Indexes.ascending("fieldName"));
	    }
	
	

}
