package com.vancone.monster.code.codegen.model.module.impl;

import com.vancone.monster.code.codegen.model.module.Module;
import lombok.Data;

@Data
public class VueElementAdminModule extends Module {
    private String author;
    private String email;
    private String license = "MIT";
}
