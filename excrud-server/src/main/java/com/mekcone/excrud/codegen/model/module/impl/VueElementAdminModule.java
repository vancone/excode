package com.mekcone.excrud.codegen.model.module.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.model.module.Module;
import lombok.Data;

@Data
public class VueElementAdminModule implements Module {

    @JacksonXmlProperty(isAttribute = true)
    private boolean use;

    @Override
    public String type() {
        return ModuleType.VUE_ELEMENT_ADMIN;
    }
}
