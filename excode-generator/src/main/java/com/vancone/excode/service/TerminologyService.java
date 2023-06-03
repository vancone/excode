package com.vancone.excode.service;

import com.vancone.excode.entity.Terminology;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author Tenton Lien
 * @since 2022/03/17
 */
@Slf4j
@Service
public class TerminologyService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void create(Terminology terminology) {
        mongoTemplate.save(terminology);
    }

    public Terminology query(String id) {
        return mongoTemplate.findById(id, Terminology.class);
    }

    public String get(String key, String language) {
        Query query = Query.query(new Criteria().andOperator(
                Criteria.where("key").is(key),
                Criteria.where("language").is(language)));
        Terminology terminology = mongoTemplate.findOne(query, Terminology.class);
        if (terminology != null) {
            return terminology.getValue();
        }
        return key;
    }
}
