package com.mekcone.studio.codegen.constant;

import java.io.File;

/*
 * Author: Tenton Lien
 */

public interface UrlPath {
    String MEKCONE_STUDIO_HOME = System.getenv("MEKCONE_STUDIO_HOME") + File.separator;
    String EXAMPLE_PATH = MEKCONE_STUDIO_HOME + File.separator + "examples" + File.separator;
    String GEN_PATH = MEKCONE_STUDIO_HOME + File.separator + "gen" + File.separator;
    String MODULE_PATH = MEKCONE_STUDIO_HOME + "modules" + File.separator;
}
