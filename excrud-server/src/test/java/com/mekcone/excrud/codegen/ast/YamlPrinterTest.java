package com.mekcone.excrud.codegen.ast;

import com.github.javaparser.printer.YamlPrinter;
import com.mekcone.excrud.codegen.controller.parser.template.impl.JavaTemplate;
import com.mekcone.excrud.codegen.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

@Slf4j
public class YamlPrinterTest {

    @Test
    public void print() {
        String yamlExamplePath = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" +File.separator + "java" + File.separator +
                "com" + File.separator + "mekcone" + File.separator + "excrud" + File.separator +
                "codegen" + File.separator + "ast" + File.separator + "examples" + File.separator;

        JavaTemplate javaTemplate = new JavaTemplate(yamlExamplePath + "Swagger2ControllerExample.txt");
        YamlPrinter yamlPrinter = new YamlPrinter(true);
        log.info(yamlPrinter.output(javaTemplate.getCompilationUnit()));
    }
}
