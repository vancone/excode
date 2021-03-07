package com.vancone.monster.code.codegen.controller.executor;

import com.vancone.monster.code.constant.ModuleConstant;
import com.vancone.monster.code.constant.UrlPath;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@Slf4j
public class SpringBootExecutor {

    private String jarFilePath;

    public SpringBootExecutor(String groupId, String artifactId, String version) {
        jarFilePath = UrlPath.MEKCONE_STUDIO_HOME + "gen" + File.separator +
                groupId + "." +
                artifactId + "-" +
                version + File.separator +
                ModuleConstant.MODULE_TYPE_SPRING_BOOT + File.separator +
                "target" + File.separator +
                artifactId + "-" +
                version + ".jar";
    }

    public void run() {
        File jarFile = new File(jarFilePath);
        if (!jarFile.exists()) {
            log.error("Executable JAR file not found");
        }
        log.info("Begin running {}", jarFilePath);
        Path jarPath = jarFile.toPath();
        Path parentPath = jarPath.getParent();
        String fileName = jarPath.getFileName().toString();

        // Running
        try {
            Process child = Runtime.getRuntime().exec("java -jar " + fileName, null, parentPath.toFile());

            InputStream child_in = child.getInputStream();
            int c;
            while (true) {
                if ((c = child_in.read()) == -1) break;
                final int d = c;
                System.out.print((char) d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
