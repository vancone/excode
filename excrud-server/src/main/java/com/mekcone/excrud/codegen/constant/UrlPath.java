package com.mekcone.excrud.codegen.constant;

import java.io.File;

public interface UrlPath {
    String EXCRUD_HOME = System.getenv("EXCRUD_HOME") + File.separator;
    String EXAMPLE_PATH = EXCRUD_HOME + File.separator + "examples" + File.separator;
    String GEN_PATH = EXCRUD_HOME + File.separator + "gen" + File.separator;
    String MODULE_PATH = EXCRUD_HOME + File.separator + "modules" + File.separator;
}
