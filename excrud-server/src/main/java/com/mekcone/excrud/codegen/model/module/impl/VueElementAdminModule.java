package com.mekcone.excrud.codegen.model.module.impl;

import com.mekcone.excrud.codegen.constant.ModuleConstant;
import com.mekcone.excrud.codegen.model.module.Module;
import lombok.Data;

@Data
public class VueElementAdminModule extends Module {

    @Override
    public String getType() {
        return ModuleConstant.MODULE_TYPE_VUE_ELEMENT_ADMIN;
    }

    private String author;
    private String email;
    private String license = "MIT";
}
