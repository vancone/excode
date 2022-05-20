package com.vancone.excode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.vancone.excode.generator.Application;
import com.vancone.excode.generator.entity.PomFile;
import com.vancone.excode.generator.entity.ProjectStructure;
import com.vancone.excode.generator.entity.Template;
import com.vancone.excode.generator.enums.TemplateType;
import com.vancone.excode.generator.util.FileUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author Tenton Lien
 * @since 2021/07/24
 */
@Slf4j
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class DataInitializer {

    @Autowired
    private MongoTemplate mongoTemplate;

    final String templatePath = "../templates" + File.separator;

    @Data
    public static class TemplateConfig {
        private String directory;
        private Template template;

        @Data
        static class Template {
            @JsonProperty("static")
            List<String> staticTemplates;

            @JsonProperty("dynamic")
            Map<TemplateType, String> dynamicTemplates;


        }

        private ProjectStructure structure;
    }

    @Test
    public void importTemplate() throws JsonProcessingException {
        if (mongoTemplate.collectionExists("template")) {
            mongoTemplate.dropCollection("template");
        }
        if (mongoTemplate.collectionExists("project_structure")) {
            mongoTemplate.dropCollection("project_structure");
        }

        File[] configFiles = new File(templatePath).listFiles();
        for (File configFile: configFiles) {
            if (configFile.isFile()) {
                String moduleName = configFile.getName().substring(0, configFile.getName().lastIndexOf("."));
                log.info("Importing module: " + moduleName);
                ObjectMapper mapper = new ObjectMapper();
                TemplateConfig config = mapper.readValue(FileUtil.read(configFile.getPath()), TemplateConfig.class);

                // Import static templates
                if (config.getTemplate().getStaticTemplates() != null) {
                    for (String fileName: config.getTemplate().getStaticTemplates()) {
                        Template template = new Template();
                        template.setType(TemplateType.STATIC_TEMPLATE);
                        template.setModule(moduleName);
                        template.setFileName(fileName);
                        log.info(templatePath + moduleName + File.separator + fileName);
                        template.setContent(FileUtil.read(templatePath + moduleName + File.separator + fileName));
                        mongoTemplate.save(template);
                    }
                }

                // Import dynamic templates
                for (TemplateType key: config.getTemplate().getDynamicTemplates().keySet()) {
                    Template template = new Template();
                    template.setType(key);
                    template.setModule(moduleName);
                    template.setFileName(config.getTemplate().getDynamicTemplates().get(key));
                    log.info(templatePath + moduleName + File.separator + config.getTemplate().getDynamicTemplates().get(key));
                    template.setContent(FileUtil.read(templatePath + moduleName + File.separator + config.getTemplate().getDynamicTemplates().get(key)));
                    mongoTemplate.save(template);
                }

                // Import project structure
                if (config.getStructure() != null) {
                    mongoTemplate.save(config.getStructure());
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
                    log.info(dependencies.toString());
                } catch (JsonProcessingException e) {
                    log.info("Parse XML error while retrieving dependencies of extension \"{}\": {}", pomText, e.getMessage());
                }
            }
        }
    }

    @Test
    public void importTerminology() {

    }

    @Test
    public void importProjectFileTree() {

    }
}
