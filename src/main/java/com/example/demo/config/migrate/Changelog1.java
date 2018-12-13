package com.example.demo.config.migrate;

import org.bson.Document;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChangeLog(order = "001")
public class Changelog1 {
    
    @ChangeSet(order = "001", id = "createUserCollection", author = "sgrillon")
    public void createUserCollection(final MongoDatabase db) {
        log.info("createUserCollection - start");
        final MongoCollection<Document> userCollection = db.getCollection("user");
        userCollection.drop();
        db.createCollection("user");
        db.getCollection("user").createIndex(new BasicDBObject("email", 1), new IndexOptions().unique(true));
        log.info("createUserCollection - end");
    }
    
}
