package com.mekcone.excrud.codegen.controller.packager;

import com.mekcone.excrud.codegen.constant.ApplicationParameter;
import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.util.FileUtil;
import com.mekcone.excrud.codegen.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SpringBootPackager {

    private String projectPath;

    public SpringBootPackager(String groupId, String artifactId, String version) {
        projectPath = ApplicationParameter.EXCRUD_HOME + "gen" + File.separator +
                groupId + "." +
                artifactId + "-" +
                version + File.separator +
                ModuleType.SPRING_BOOT + File.separator;
    }

    public boolean build() {
        File file = new File(projectPath);
        if (!file.exists()) {
            LogUtil.info("Not generated yet");
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
