package com.vancone.devops.codegen.model.module;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vancone.devops.codegen.model.module.impl.*;
import com.vancone.devops.codegen.util.StrUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Module {
    private String type;
    private boolean use;

    private Map<String, String> properties = new HashMap<>();

    /*@JacksonXmlElementWrapper(localName = "extensions")
    @JacksonXmlProperty(localName = "extension")
    private List<Extension> extensions = new ArrayList<>();
    */

    @Data
    public static class Extension {
//        @JacksonXmlProperty(isAttribute = true)
        @JsonIgnore
        private String id;

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
