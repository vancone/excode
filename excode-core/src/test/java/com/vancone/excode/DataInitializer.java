package com.vancone.excode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.vancone.excode.core.model.PomFile;
import com.vancone.excode.core.model.Template;
import com.vancone.excode.core.TemplateFactory;
import com.vancone.excode.core.util.FileUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Tenton Lien
 * @date 7/24/2021
 */
@Slf4j
public class DataInitializer {

    private MongoTemplate mongoTemplate = TemplateFactory.getMongoTemplate();

    final String templatePath = "../template" + File.separator;

    @Data
    public static class TemplateConfig {
        Map<String, String> template;
    }

    @Test
    public void importTemplate() throws JsonProcessingException {
        if (mongoTemplate.collectionExists("template")) {
            mongoTemplate.dropCollection("template");
        }

        File[] configFiles = new File(templatePath).listFiles();
        for (File configFile: configFiles) {
            if (configFile.isFile()) {
                String moduleName = configFile.getName().substring(0, configFile.getName().lastIndexOf("."));
                System.out.println("Importing module: " + moduleName);
                ObjectMapper mapper = new ObjectMapper();
                TemplateConfig config = mapper.readValue(FileUtil.read(configFile.getPath()), TemplateConfig.class);
                for (String key: config.getTemplate().keySet()) {
                    Template template = new Template();
                    template.setName(key);
                    System.out.println(templatePath + moduleName + File.separator + config.getTemplate().get(key));
                    template.setContent(FileUtil.read(templatePath + moduleName + File.separator + config.getTemplate().get(key)));
                    mongoTemplate.save(template);
                }

            }
        }
    }

    @Test
    public void importPom() {
        if (mongoTemplate.collectionExists("spring_boot_pom")) {
            mongoTemplate.dropCollection("spring_boot_pom");
        }

        File[] pomFiles = new File(templatePath + "spring-boot" + File.separator + "pom").listFiles();
        for (File pomFile: pomFiles) {
            if (pomFile.isFile()) {
                XmlMapper xmlMapper = new XmlMapper();
                String pomText = FileUtil.read(pomFile.getPath());

                try {
                    List<PomFile.Dependency> dependencies = xmlMapper.readValue(pomText, new TypeReference<List<PomFile.Dependency>>() {});
                    for (PomFile.Dependency dependency: dependencies) {
                        dependency.setLabel(pomFile.getName().substring(0, pomFile.getName().lastIndexOf(".")));
                        mongoTemplate.save(dependency);
                    }
                    System.out.println(dependencies);
                } catch (JsonProcessingException e) {
                    log.info("Parse XML error while retrieving dependencies of extension \"{}\": {}", pomText, e.getMessage());
                }
            }
        }
    }
}
