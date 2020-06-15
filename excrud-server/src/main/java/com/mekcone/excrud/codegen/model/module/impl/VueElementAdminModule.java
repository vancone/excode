package com.mekcone.excrud.codegen.model.module.impl;

import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.model.module.Module;
import lombok.Data;

@Data
public class VueElementAdminModule extends Module {

    @Override
    public String getType() {
        return ModuleType.VUE_ELEMENT_ADMIN;
    }

    private String author;
    private String email;
    private String license = "MIT";
}
