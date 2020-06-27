package com.mekcone.excrud.web.repository;

import com.mekcone.excrud.host.entity.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HostRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Host> findAll() {
        List<Host> hosts = mongoTemplate.findAll(Host.class, "server");
        return hosts;
    }
}
