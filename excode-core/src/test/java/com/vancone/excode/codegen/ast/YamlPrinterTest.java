package com.vancone.excode.codegen.ast;

import com.github.javaparser.printer.YamlPrinter;
import com.vancone.excode.core.controller.parser.template.impl.JavaTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

@Slf4j
public class YamlPrinterTest {

    private final String SWAGGER2_CONTROLLER_EXAMPLE = "Swagger2ControllerExample";
    private final String CROSS_ORIGIN_CONFIG_EXAMPLE = "CrossOriginConfigExample";

    private String currentExampleFileName = CROSS_ORIGIN_CONFIG_EXAMPLE;

    @Test
    public void print() {
        String yamlExamplePath = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" +File.separator + "java" + File.separator +
                "com" + File.separator + "mekcone" + File.separator + "excrud" + File.separator +
                "codegen" + File.separator + "ast" + File.separator + "examples" + File.separator;

        JavaTemplate javaTemplate = new JavaTemplate(yamlExamplePath + currentExampleFileName + ".txt");
        YamlPrinter yamlPrinter = new YamlPrinter(true);
        log.info(yamlPrinter.output(javaTemplate.getCompilationUnit()));
    }
}
