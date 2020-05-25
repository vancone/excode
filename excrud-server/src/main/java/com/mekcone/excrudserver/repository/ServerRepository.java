package com.mekcone.excrudserver.repository;

import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrudserver.entity.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServerRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Server> findAll() {
        List<Server> servers = mongoTemplate.findAll(Server.class, "server");
        return servers;
    }
}
