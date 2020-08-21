package com.mekcone.studio.codegen.model.module;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.studio.codegen.model.module.impl.*;
import com.mekcone.studio.codegen.util.StrUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Module {
    private String type;
    private boolean use;

    @JacksonXmlElementWrapper(localName = "extensions")
    @JacksonXmlProperty(localName = "extension")
    private List<Extension> extensions = new ArrayList<>();

    @Data
    public static class Extension {
        @JacksonXmlProperty(isAttribute = true)
        private String id;

        @JacksonXmlProperty(isAttribute = true)
        private boolean use;
    }

    public Module() {
        String callerClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        type = StrUtil.kebabCase(callerClassName.substring(callerClassName.lastIndexOf('.') + 1, callerClassName.length() - 6));
    }

    public DeploymentModule asDeploymentModule() {
        return (DeploymentModule)this;
    }

    public DocumentModule asDocumentModule() {
        return (DocumentModule)this;
    }

    public SpringBootModule asSpringBootModule() {
        return (SpringBootModule)this;
    }

    public VueElementAdminModule asVueElementAdminModule() {
        return (VueElementAdminModule)this;
    }

    public WebsitePageModule asWebsitePageModule() {
        return (WebsitePageModule)this;
    }
}
