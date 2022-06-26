package com.vancone.excode.service.microservice;

import com.vancone.excode.entity.Output;
import com.vancone.excode.entity.Template;
import com.vancone.excode.entity.microservice.SpringBootMicroservice;
import com.vancone.excode.enums.SpringProfile;
import com.vancone.excode.enums.TemplateType;
import com.vancone.excode.service.TemplateService;
import com.vancone.excode.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tenton Lien
 * @since 2022/05/19
 */
@Service
public class SpringBootMicroserviceGenerator {

    @Autowired
    private ProjectObjectModelService projectObjectModelService;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

    @Autowired
    private TemplateService templateService;

    public Output createPom(SpringBootMicroservice microservice) {
        return projectObjectModelService.generate(microservice);
    }

    public Output createProperty(SpringBootMicroservice microservice) {

        return applicationPropertyService.generate(SpringProfile.DEV, microservice);
    }

    public Output createApplicationEntry(SpringBootMicroservice microservice) {
        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_APPLICATION_ENTRY);
        templateService.preProcess(microservice, template);
        return new Output(TemplateType.SPRING_BOOT_APPLICATION_ENTRY,
                getPackagePath(microservice) + StrUtil.toPascalCase(microservice.getArtifactId()) + "Application.java",
                template.getContent());
    }

    private String getPackagePath(SpringBootMicroservice microservice) {
        return microservice.getName() + "/src/main/java/" +
                microservice.getGroupId().replace(".", "/") + "/" +
                microservice.getArtifactId().replace(".", "/").replace('-', '/') + "/";
    }
}
