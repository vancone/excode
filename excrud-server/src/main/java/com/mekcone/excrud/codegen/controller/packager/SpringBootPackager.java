package com.mekcone.excrud.codegen.controller.packager;

import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.constant.UrlPath;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class SpringBootPackager {

    private String projectPath;

    public SpringBootPackager(String groupId, String artifactId, String version) {
        projectPath = UrlPath.EXCRUD_HOME + "gen" + File.separator +
                groupId + "." +
                artifactId + "-" +
                version + File.separator +
                ModuleType.SPRING_BOOT + File.separator;
    }

    public boolean build() {
        File file = new File(projectPath);
        if (!file.exists()) {
            log.info("Not generated yet");
            return false;
        }

        // Compiling
        try {
            // Only work on Windows
            Process child = Runtime.getRuntime().exec("mvn.cmd clean package", null, file);

            InputStream child_in = child.getInputStream();
            int c;
            while (true) {
                if ((c = child_in.read()) == -1) break;
                final int d = c;
                System.out.print((char) d);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}