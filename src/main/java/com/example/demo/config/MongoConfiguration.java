package com.example.demo.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.example.demo.config.migrate.Changelog1;
import com.github.cloudyrock.mongock.SpringBootMongock;
import com.github.cloudyrock.mongock.SpringBootMongockBuilder;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoAuditing
public class MongoConfiguration {

    @Bean
    public SpringBootMongock mongock(ApplicationContext springContext, MongoClient mongoClient) {
      return (SpringBootMongock) new SpringBootMongockBuilder(mongoClient, "sgrillon", Changelog1.class.getPackage().getName())
          .setApplicationContext(springContext) 
          .setLockQuickConfig()
          .build();
    }
    
}
