package com.vancone.excode.core;

import com.vancone.excode.core.enums.TemplateName;
import com.vancone.excode.core.model.Template;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author Tenton Lien
 * @date 7/24/2021
 */
public class TemplateFactory {

    private static MongoTemplate mongoTemplate = new MongoTemplate(
            new SimpleMongoClientDatabaseFactory("mongodb://localhost:27017/excode"));

    public static MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public static Template getTemplate(TemplateName name) {
        return mongoTemplate.findOne(Query.query(Criteria.where("name").is(name)), Template.class);
    }
}
