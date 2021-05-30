package com.vancone.devops.codegen.controller.packager;

import com.vancone.devops.constant.ModuleConstant;
import com.vancone.devops.constant.UrlPath;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/*
 * Author: Tenton Lien
 */

@Slf4j
public class SpringBootPackager {

    private String projectPath;

    public SpringBootPackager(String groupId, String artifactId, String version) {
        projectPath = UrlPath.VANCONE_STUDIO_HOME + "gen" + File.separator +
                groupId + "." +
                artifactId + "-" +
                version + File.separator +
                ModuleConstant.MODULE_TYPE_SPRING_BOOT + File.separator;
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
