package com.vancone.excode.executor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Tenton Lien
 * @since 8/1/2021
 */
@Service
public class ProgramService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /*public Page<Program> query() {
        PageRequest page = PageRequest.of(0, 10);
        List<Program> programs = mongoTemplate.findAll(Program.class);
        return new PageImpl<Program>();
    }*/
}
