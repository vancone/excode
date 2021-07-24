package com.vancone.excode.core.old.model.module.impl;

import com.vancone.excode.core.old.model.module.ModuleOld;
import lombok.Data;

/**
 * @author Tenton Lien
 */
@Data
public class VueElementAdminModule extends ModuleOld {
    private String author;
    private String email;
    private String license = "MIT";
}
