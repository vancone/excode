package com.mekcone.excrud.codegen.model.project;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.codegen.model.module.Module;
import com.mekcone.excrud.codegen.model.module.impl.DeploymentModule;
import com.mekcone.excrud.codegen.model.module.impl.VueElementAdminModule;
import com.mekcone.excrud.codegen.model.module.impl.WebsitePageModule;
import com.mekcone.excrud.codegen.model.module.impl.DocumentModule;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.RelationalDatabaseModule;
import com.mekcone.excrud.codegen.model.module.impl.springboot.SpringBootModule;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModuleSet {
    @JacksonXmlProperty(localName = "document")
    private DocumentModule documentModule;

    @JacksonXmlProperty(localName = "deployment")
    private DeploymentModule deploymentModule;

    @JacksonXmlProperty(localName = "relational-database")
    private RelationalDatabaseModule relationalDatabaseModule;

    @JacksonXmlProperty(localName = "spring-boot")
    private SpringBootModule springBootModule;

    @JacksonXmlProperty(localName = "vue-element-admin")
    private VueElementAdminModule vueElementAdminModule;

    @JacksonXmlProperty(localName = "website-page")
    private WebsitePageModule websitePageModule;

    public List<Module> asList() {
        List<Module> modules = new ArrayList<>();
        modules.add(documentModule);
        modules.add(deploymentModule);
        modules.add(relationalDatabaseModule);
        modules.add(springBootModule);
        modules.add(vueElementAdminModule);
        modules.add(websitePageModule);
        return modules;
    }
}
