package com.mekcone.excrud.codegen.model.module.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.model.module.Module;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeploymentModule extends Module {

    @JacksonXmlElementWrapper(localName = "operating-systems")
    @JacksonXmlProperty(localName = "operating-system")
    private List<OperatingSystem> operatingSystems = new ArrayList<>();

    @JacksonXmlProperty(localName = "enable-docker")
    private boolean enableDocker;


    @Override
    public String getType() {
        return ModuleType.DEPLOYMENT;
    }

    @Data
    public static class OperatingSystem {
        private String type;
        private String version;
    }
}
