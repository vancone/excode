package com.vancone.excode.generator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancone.cloud.common.model.ResponsePage;
import com.vancone.excode.generator.entity.*;
import com.vancone.excode.generator.enums.TemplateType;
import com.vancone.excode.generator.generator.MicroserviceSpringBootGenerator;
import com.vancone.excode.generator.repository.MicroserviceSpringBootRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Slf4j
@Service
public class MicroserviceSpringBootService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MicroserviceSpringBootGenerator generator;

    @Autowired
    private MicroserviceSpringBootRepository microserviceSpringBootRepository;

    public Microservice create(MicroserviceSpringBoot microserviceSpringBoot) {
        return microserviceSpringBootRepository.save(microserviceSpringBoot);
    }

    public void update(MicroserviceSpringBoot microserviceSpringBoot) {
        microserviceSpringBootRepository.save(microserviceSpringBoot);
    }

    public MicroserviceSpringBoot query(String id) {
        return microserviceSpringBootRepository.findById(id).get();
    }

    public ResponsePage<MicroserviceSpringBoot> queryPage(int pageNo, int pageSize, String search, String projectId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        MicroserviceSpringBoot example = new MicroserviceSpringBoot();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains);
        if (StringUtils.isNotBlank(search)) {
            example.setName(search);
        }
        ProjectNew project = new ProjectNew();
        project.setId(projectId);
        example.setProject(project);
        return new ResponsePage<>(microserviceSpringBootRepository.findAll(Example.of(example, matcher), pageable));
    }

    public void delete(String id) {
        microserviceSpringBootRepository.deleteById(id);
    }

    public ProjectStructure queryStructure(String microserviceId) {

        Criteria criteria = Criteria.where("module").is("spring-boot");
        ProjectStructure structure = mongoTemplate.findOne(new Query().addCriteria(criteria), ProjectStructure.class);
        if (structure != null) {
            Optional<MicroserviceSpringBoot> optional = microserviceSpringBootRepository.findById(microserviceId);
            if (optional.isPresent()) {
                MicroserviceSpringBoot microservice = optional.get();
                ObjectMapper mapper = new ObjectMapper();
                try {
                    String json = mapper.writeValueAsString(structure);
                    json = json.replaceAll("\\$projectName", microservice.getName());
                    json = json.replaceAll("\\$groupId", microservice.getGroupId());
                    json = json.replaceAll("\\$artifactId", microservice.getArtifactId());
                    structure = mapper.readValue(json, ProjectStructure.class);
                } catch (JsonProcessingException e) {
                    log.error("Failed to process project structure of microservice(id={})", microserviceId);
                }
            }
        }
        return structure;
    }

    public Output generate(String microserviceId, TemplateType type) {
        Optional<MicroserviceSpringBoot> optional = microserviceSpringBootRepository.findById(microserviceId);
        if (optional.isPresent()) {
            MicroserviceSpringBoot microservice = optional.get();
            switch (type) {
                case SPRING_BOOT_POM:
                    return generator.createPom(microservice);
                case SPRING_BOOT_PROPERTIES:
                    return generator.createProperty(microservice);
                case SPRING_BOOT_APPLICATION_ENTRY:
                    return generator.createApplicationEntry(microservice);
                default:
                    return null;
            }
        } else {
            log.error("Microservice {} not found", microserviceId);
            return null;
        }
    }
}
