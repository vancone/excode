package com.vancone.excode.core;

import com.vancone.excode.core.enums.TemplateType;
import com.vancone.excode.core.model.Project;
import com.vancone.excode.core.model.Template;
import com.vancone.excode.core.util.StrUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static Template getTemplate(TemplateType name) {
        return mongoTemplate.findOne(Query.query(Criteria.where("name").is(name)), Template.class);
    }

    public static void preProcess(Project project, Template template) {
        template.replace("groupId", project.getGroupId());
        template.replace("artifactId", project.getArtifactId());
        template.replace("ArtifactId", StrUtil.capitalize(project.getArtifactId()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        template.replace("date", formatter.format(LocalDateTime.now()));
    }
}
