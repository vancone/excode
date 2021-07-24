package com.vancone.excode.codegen.model.module.impl;

import com.vancone.excode.codegen.model.module.Module;
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