package com.mekcone.excrud.database;

import com.mekcone.excrud.database.mongodb.MongoDuplicator;
import org.junit.jupiter.api.Test;

public class MongoTest {

    @Test
    public void testConnection() {
        MongoDuplicator mongoDuplicator = new MongoDuplicator("localhost", 27017);
//        mongoDuplicator.exportCollection("mekcone_excrud", "project");
        mongoDuplicator.exportDatabase("systemtable");
    }
}
