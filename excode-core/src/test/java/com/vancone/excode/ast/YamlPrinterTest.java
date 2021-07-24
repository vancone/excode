package com.vancone.excode.ast;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.printer.YamlPrinter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

@Slf4j
public class YamlPrinterTest {

    private final String SWAGGER2_CONTROLLER_EXAMPLE = "Swagger2ControllerExample";
    private final String CROSS_ORIGIN_CONFIG_EXAMPLE = "CrossOriginConfigExample";

    private String currentExampleFileName = CROSS_ORIGIN_CONFIG_EXAMPLE;

    @Test
    public void print() throws FileNotFoundException {
        String yamlExamplePath = System.getProperty("user.dir") + File.separator +
                "src" + File.separator + "test" +File.separator + "java" + File.separator +
                "com" + File.separator + "vancone" + File.separator + "excode" + File.separator +
                "ast" + File.separator + "examples" + File.separator;

        CompilationUnit unit = StaticJavaParser.parse(new File(yamlExamplePath + currentExampleFileName + ".txt"));
        YamlPrinter yamlPrinter = new YamlPrinter(true);
        log.info(yamlPrinter.output(unit));
    }
}
