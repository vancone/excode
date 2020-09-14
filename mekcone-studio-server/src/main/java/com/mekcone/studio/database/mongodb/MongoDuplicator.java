package com.mekcone.studio.database.mongodb;

import com.mongodb.client.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

/*
 * Author: Tenton Lien
 * Date: 7/1/2020
 */

@Slf4j
public class MongoDuplicator {

    private String url;

    private String password;

    private String collection;

    private MongoClient mongoClient;

    private int limitOfEachCollection = 0;  // value 0 means no limits

    // No authentication
    public MongoDuplicator(String url, int port) {
        mongoClient = MongoClients.create("mongodb://" + url + ":" + port);
    }

    public void setLimit(int limit) {
        limitOfEachCollection = limit > 0 ? limit : limit;
    }

    public void exportDatabase(String databaseName) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
        for (String collectionName: mongoDatabase.listCollectionNames()) {
            exportCollection(databaseName, collectionName);
        }
    }

    private final String tempOutputPath = "D:\\";

    public void exportCollection(String databaseName, String collectionName) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
        MongoCollection mongoCollection = mongoDatabase.getCollection(collectionName);
        FindIterable<Document> findIterable;
        if (limitOfEachCollection > 0) {
            findIterable = mongoCollection.find().limit(limitOfEachCollection);
        } else {
            findIterable = mongoCollection.find();
        }
        StringBuilder content = new StringBuilder("[");
        for (Document document: findIterable) {
            content.append(document.toJson()).append(",");
        }
        content.setCharAt(content.length() - 1, ']');
        log.info("Output: {}", content.toString());
    }

    public void importDatabase() {

    }

    public void importCollection() {

    }
}
