package com.vancone.excode.generator.generator;

import com.vancone.excode.generator.entity.*;
import com.vancone.excode.generator.enums.DataCarrier;
import com.vancone.excode.generator.enums.TemplateType;
import com.vancone.excode.generator.service.TemplateService;
import com.vancone.excode.generator.util.PropertiesParser;
import com.vancone.excode.generator.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 2022/05/19
 */
@Service
public class MicroserviceSpringBootGenerator {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TemplateService templateService;

    public Output createPom(MicroserviceSpringBoot microservice) {
        PomFile pom = new PomFile(microservice);
        addDependencyByLabel(pom, "default");
        addDependencyByLabel(pom, "mybatis");
//        if (module.getOrmType() == OrmType.MYBATIS_ANNOTATION) {
//            addDependencyByLabel(pom, "mybatis");
//        } else if (module.getOrmType() == OrmType.SPRING_DATA_JPA) {
//            addDependencyByLabel(pom, "spring-data-jpa");
//        }
//        for (Project.DataAccess.Solution.JavaSpringBoot.Extension extension: module.getExtensions()) {
//            addDependencyByLabel(pom, extension.getId());
//        }
        return new Output(TemplateType.SPRING_BOOT_POM, microservice.getName() + File.separator + "pom.xml", pom.toString());
    }

    private void addDependencyByLabel(PomFile pom, String label) {
        List<PomFile.Dependency> dependencies =
                mongoTemplate.find(Query.query(Criteria.where("label").is(label)), PomFile.Dependency.class);
        if (!dependencies.isEmpty()) {
            pom.getDependencies().addAll(dependencies);
        }
    }

    public Output createProperty(MicroserviceSpringBoot microservice) {
        PropertiesParser parser = new PropertiesParser();
        parser.add("spring.application.name", microservice.getArtifactId());

        String port = (microservice.getServerPort() == null) ? "8080" : microservice.getServerPort().toString();
        parser.add("server.port", port);
        parser.addSeparator();

        // MySQL
//        if (!project.getDataAccess().getDataStoreByCarrier(DataCarrier.MYSQL).isEmpty()) {
//            DataStore.Connection connection = project.getDataAccess().getDataStoreByCarrier(DataCarrier.MYSQL).get(0).getConnection();
//            String datasourceUrl = "jdbc:mysql://" + connection.getHost() + ":" + connection.getPort() + "/" + connection.getDatabase();
//            datasourceUrl += connection.getTimezone() != null ? "?serverTimezone=" + connection.getTimezone() : "";
//            parser.add("spring.datasource.url", datasourceUrl);
//            parser.add("spring.datasource.username", connection.getUsername());
//            parser.add("spring.datasource.password", connection.getPassword());
//        }

        return new Output(TemplateType.SPRING_BOOT_PROPERTIES,
                microservice.getName() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties",
                parser.generate());
    }

    public Output createApplicationEntry(MicroserviceSpringBoot microservice) {
        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_APPLICATION_ENTRY);
        templateService.preProcess(microservice, template);
        return new Output(TemplateType.SPRING_BOOT_APPLICATION_ENTRY,
                getPackagePath(microservice) + StrUtil.toPascalCase(microservice.getArtifactId()) + "Application.java",
                template.getContent());
    }

    private String getPackagePath(MicroserviceSpringBoot microservice) {
        return microservice.getName() + File.separator +
                "src" + File.separator + "main" + File.separator + "java" + File.separator +
                microservice.getGroupId().replace(".", File.separator) + File.separator +
                microservice.getArtifactId().replace(".", File.separator).replace('-', File.separatorChar) + File.separator;
    }
}
