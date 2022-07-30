package com.vancone.excode.service.microservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancone.cloud.common.model.ResponsePage;
import com.vancone.excode.entity.*;
import com.vancone.excode.entity.microservice.Microservice;
import com.vancone.excode.entity.microservice.SpringBootMicroservice;
import com.vancone.excode.enums.TemplateType;
import com.vancone.excode.repository.DataStoreRelationalRepository;
import com.vancone.excode.repository.SpringBootMicroserviceRepository;
import com.vancone.excode.util.StrUtil;
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

import java.util.List;
import java.util.Optional;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Slf4j
@Service
public class SpringBootMicroserviceService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SpringBootMicroserviceGenerator generator;

    @Autowired
    private DataStoreRelationalRepository dataStoreRelationalRepository;

    @Autowired
    private SpringBootMicroserviceRepository springBootMicroserviceRepository;

    public Microservice create(SpringBootMicroservice springBootMicroservice) {
        return springBootMicroserviceRepository.save(springBootMicroservice);
    }

    public void update(SpringBootMicroservice springBootMicroservice) {
        springBootMicroserviceRepository.save(springBootMicroservice);
    }

    public SpringBootMicroservice query(String id) {
        return springBootMicroserviceRepository.findById(id).orElse(null);
    }

    public ResponsePage<SpringBootMicroservice> queryPage(int pageNo, int pageSize, String search, String projectId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        SpringBootMicroservice example = new SpringBootMicroservice();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains);
        if (StringUtils.isNotBlank(search)) {
            example.setName(search);
        }
        Project project = new Project();
        project.setId(projectId);
        example.setProject(project);
        return new ResponsePage<>(springBootMicroserviceRepository.findAll(Example.of(example, matcher), pageable));
    }

    public void delete(String id) {
        springBootMicroserviceRepository.deleteById(id);
    }

    public ProjectStructure queryStructure(String microserviceId) {

        Criteria criteria = Criteria.where("module").is("spring-boot");
        ProjectStructure structure = mongoTemplate.findOne(new Query().addCriteria(criteria), ProjectStructure.class);
        if (structure != null) {

            Optional<SpringBootMicroservice> optional = springBootMicroserviceRepository.findById(microserviceId);
            if (optional.isPresent()) {
                SpringBootMicroservice microservice = optional.get();
                List<DataStoreRelational> dataStores = dataStoreRelationalRepository.findByProjectId(microservice.getProject().getId());

                for (DataStoreRelational dataStore: dataStores) {
                    structure.addNode("$projectName__src__main__java__$groupId.$artifactId__controller",
                            new ProjectStructure(StrUtil.capitalize(dataStore.getName()) + "Controller",
                                    TemplateType.SPRING_BOOT_CONTROLLER));

                    structure.addNode("$projectName__src__main__java__$groupId.$artifactId__entity",
                            new ProjectStructure(StrUtil.capitalize(dataStore.getName()),
                                    TemplateType.SPRING_BOOT_ENTITY));

                    structure.addNode("$projectName__src__main__java__$groupId.$artifactId__service",
                            new ProjectStructure(StrUtil.capitalize(dataStore.getName()) + "Service",
                                    TemplateType.SPRING_BOOT_CONTROLLER));
                }

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

    public Output generate(String microserviceId, TemplateType type, String fileName) {
        Optional<SpringBootMicroservice> optional = springBootMicroserviceRepository.findById(microserviceId);
        if (optional.isPresent()) {
            SpringBootMicroservice microservice = optional.get();
            switch (type) {
                case SPRING_BOOT_POM:
                    return generator.createPom(microservice);
                case SPRING_BOOT_PROPERTIES:
                    return generator.createProperty(microservice);
                case SPRING_BOOT_APPLICATION_ENTRY:
                    return generator.createApplicationEntry(microservice);
                case SPRING_BOOT_CONTROLLER:
                    String dataStoreName = fileName.replace("Controller", "").toLowerCase();
                    return generator.createController(microservice, dataStoreName);
                default:
                    return null;
            }
        } else {
            log.error("Microservice {} not found", microserviceId);
            return null;
        }
    }
}
