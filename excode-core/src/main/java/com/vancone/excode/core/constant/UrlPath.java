package com.vancone.excode.core.constant;

import java.io.File;

/**
 * @author Tenton Lien
 */
public interface UrlPath {
    String EXCRUD_HOME = System.getenv("EXCRUD_HOME") + File.separator;
    String EXAMPLE_PATH = EXCRUD_HOME + File.separator + "examples" + File.separator;
    String GEN_PATH = EXCRUD_HOME + File.separator + "gen" + File.separator;
    String MODULE_PATH = EXCRUD_HOME + "modules" + File.separator;
}
