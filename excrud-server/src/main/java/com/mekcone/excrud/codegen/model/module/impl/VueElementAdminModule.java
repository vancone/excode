package com.mekcone.excrud.codegen.model.module.impl;

import com.mekcone.excrud.codegen.constant.ModuleConstant;
import com.mekcone.excrud.codegen.model.module.Module;
import lombok.Data;

@Data
public class VueElementAdminModule extends Module {
    private String author;
    private String email;
    private String license = "MIT";
}
