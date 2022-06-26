package com.vancone.excode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancone.excode.entity.ProjectStructure;
import com.vancone.excode.entity.Template;
import com.vancone.excode.enums.TemplateType;
import com.vancone.excode.service.microservice.ProjectObjectModelService;
import com.vancone.excode.util.FileUtil;
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

    @Autowired
    private ProjectObjectModelService projectObjectModelService;

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
        projectObjectModelService.initData();
    }

    @Test
    public void importTerminology() {

    }

    @Test
    public void importProjectFileTree() {

    }
}
