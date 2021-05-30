package com.vancone.devops.constant;

import java.io.File;

/**
 * @author Tenton Lien
 */
public interface UrlPath {
    String VANCONE_STUDIO_HOME = System.getenv("VANCONE_STUDIO_HOME") + File.separator;
    String EXAMPLE_PATH = VANCONE_STUDIO_HOME + File.separator + "examples" + File.separator;
    String GEN_PATH = VANCONE_STUDIO_HOME + File.separator + "gen" + File.separator;
    String MODULE_PATH = VANCONE_STUDIO_HOME + "modules" + File.separator;
}
