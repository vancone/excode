package com.vancone.excode.generator.codegen.model.module.impl;

import com.vancone.excode.generator.codegen.model.module.Module;
import lombok.Data;

/**
 * @author Tenton Lien
 */
@Data
public class VueElementAdminModule extends Module {
    private String author;
    private String email;
    private String license = "MIT";
}