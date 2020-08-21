package com.mekcone.studio.codegen.model.module.impl;

import com.mekcone.studio.codegen.model.module.Module;
import lombok.Data;

@Data
public class VueElementAdminModule extends Module {
    private String author;
    private String email;
    private String license = "MIT";
}
