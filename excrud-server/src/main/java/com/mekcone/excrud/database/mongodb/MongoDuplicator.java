package com.mekcone.excrud.database.mongodb;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.*;
import com.mongodb.connection.ClusterDescription;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

public class MongoDuplicator {

    private String url;

    private String password;

    private String collection;

    public MongoDuplicator() {
        MongoClient mongoClient = new MongoClient() {
            @Override
            public MongoDatabase getDatabase(String s) {
                return null;
            }

            @Override
            public ClientSession startSession() {
                return null;
            }

            @Override
            public ClientSession startSession(ClientSessionOptions clientSessionOptions) {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public MongoIterable<String> listDatabaseNames() {
                return null;
            }

            @Override
            public MongoIterable<String> listDatabaseNames(ClientSession clientSession) {
                return null;
            }

            @Override
            public ListDatabasesIterable<Document> listDatabases() {
                return null;
            }

            @Override
            public ListDatabasesIterable<Document> listDatabases(ClientSession clientSession) {
                return null;
            }

            @Override
            public <TResult> ListDatabasesIterable<TResult> listDatabases(Class<TResult> aClass) {
                return null;
            }

            @Override
            public <TResult> ListDatabasesIterable<TResult> listDatabases(ClientSession clientSession, Class<TResult> aClass) {
                return null;
            }

            @Override
            public ChangeStreamIterable<Document> watch() {
                return null;
            }

            @Override
            public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> aClass) {
                return null;
            }

            @Override
            public ChangeStreamIterable<Document> watch(List<? extends Bson> list) {
                return null;
            }

            @Override
            public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> list, Class<TResult> aClass) {
                return null;
            }

            @Override
            public ChangeStreamIterable<Document> watch(ClientSession clientSession) {
                return null;
            }

            @Override
            public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, Class<TResult> aClass) {
                return null;
            }

            @Override
            public ChangeStreamIterable<Document> watch(ClientSession clientSession, List<? extends Bson> list) {
                return null;
            }

            @Override
            public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, List<? extends Bson> list, Class<TResult> aClass) {
                return null;
            }

            @Override
            public ClusterDescription getClusterDescription() {
                return null;
            }
        };
    }

    public void copy() {

    }

    public void paste() {

    }
}
