package com.vancone.devops.database;

import com.vancone.devops.util.MongoDuplicator;
import org.junit.jupiter.api.Test;

public class MongoTest {

    MongoDuplicator mongoDuplicator;

    @Test
    public void initialize() {
        final String MONGODB_HOST = "localhost";
        final int MONGODB_PORT = 27017;
        mongoDuplicator = new MongoDuplicator(MONGODB_HOST, MONGODB_PORT);
    }

    @Test
    public void exportSingleCollection() {
        initialize();
        final String DATABASE_NAME = "vancone_devops";
        final String COLLECTION_NAME = "project";
        mongoDuplicator.exportCollection(DATABASE_NAME, COLLECTION_NAME);
    }

    @Test
    public void exportSingleDatabase() {
        initialize();
        final String DATABASE_NAME = "systemtable";
        mongoDuplicator.exportDatabase(DATABASE_NAME);
    }
}