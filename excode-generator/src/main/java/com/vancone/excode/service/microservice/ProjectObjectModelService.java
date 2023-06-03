package com.vancone.excode.service.microservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.vancone.excode.entity.Output;
import com.vancone.excode.entity.microservice.ProjectObjectModel;
import com.vancone.excode.entity.microservice.SpringBootMicroservice;
import com.vancone.excode.enums.ResponseEnum;
import com.vancone.excode.enums.TemplateType;
import com.vancone.excode.exception.ResponseException;
import com.vancone.excode.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 2022/06/11
 */
@Slf4j
@Service
public class ProjectObjectModelService {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "spring_boot_pom";

    private static final String TEMPLATE_PATH = "../templates/";

    public void initData() {
        if (mongoTemplate.collectionExists(COLLECTION_NAME)) {
            mongoTemplate.dropCollection(COLLECTION_NAME);
        }

        File[] pomFiles = new File(TEMPLATE_PATH + "spring-boot/pom").listFiles();
        for (File pomFile: pomFiles) {
            if (pomFile.isFile()) {
                XmlMapper xmlMapper = new XmlMapper();
                String pomText = FileUtil.read(pomFile.getPath());

                try {
                    List<ProjectObjectModel.Dependency> dependencies = xmlMapper.readValue(pomText, new TypeReference<List<ProjectObjectModel.Dependency>>() {});
                    for (ProjectObjectModel.Dependency dependency: dependencies) {
                        dependency.setLabel(pomFile.getName().substring(0, pomFile.getName().lastIndexOf(".")));
                        mongoTemplate.save(dependency);
                    }
                    log.info(dependencies.toString());
                } catch (JsonProcessingException e) {
                    log.info("Parse XML error while retrieving dependencies of extension \"{}\": {}", pomText, e.getMessage());
                }
            }
        }
    }

    public void addDependencyByLabel(ProjectObjectModel pom, String label) {
        List<ProjectObjectModel.Dependency> dependencies =
                mongoTemplate.find(Query.query(Criteria.where("label").is(label)), ProjectObjectModel.Dependency.class);
        if (!dependencies.isEmpty()) {
            pom.getDependencies().addAll(dependencies);
        }
    }

    public Output generate(SpringBootMicroservice microservice) {
        ProjectObjectModel model = new ProjectObjectModel(microservice);

        addDependencyByLabel(model, "default");

        if (microservice.getOrmType() == null) {
            throw new ResponseException(ResponseEnum.ORM_TYPE_REQUIRED);
        }
        switch (microservice.getOrmType()) {
            case MYBATIS_ANNOTATION:
            case MYBATIS_XML:
            case MYBATIS_PLUS:
                addDependencyByLabel(model, "mybatis");
                break;
            case SPRING_DATA_JPA:
                addDependencyByLabel(model, "spring-data-jpa");
                break;
            default:
                break;
        }

        ObjectMapper mapper = new XmlMapper()
                .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            return new Output(TemplateType.SPRING_BOOT_POM, microservice.getName() + "/pom.xml", mapper.writeValueAsString(model));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
