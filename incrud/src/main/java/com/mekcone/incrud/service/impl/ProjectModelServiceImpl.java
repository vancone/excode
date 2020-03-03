package com.mekcone.incrud.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekcone.incrud.model.project.ProjectModel;
import com.mekcone.incrud.model.project.components.Column;
import com.mekcone.incrud.model.project.components.Table;
import com.mekcone.incrud.service.ProjectModelService;
import com.mekcone.incrud.util.FileUtil;
import com.mekcone.incrud.util.LogUtil;
import com.mekcone.incrud.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ProjectModelServiceImpl implements ProjectModelService {
    @Autowired
    private ProjectModel projectModel;


    @Override
    public ProjectModel getProjectModel() {
        return this.projectModel;
    }


    @Override
    public boolean load() {
        String projectModelPath = PathUtil.getPath("PROJECT_MODEL_PATH");
        if (projectModelPath != null) {
            return load(projectModelPath);
        } else {
            LogUtil.warn("No default project model path set");
            return false;
        }
    }


    @Override
    public boolean load(String path) {
        PathUtil.addPath("PROJECT_MODEL_PATH", path);
        File file = new File(path);
        PathUtil.addPath("EXPORT_PATH", file.getParent() + "/" + file.getName().substring(0, file.getName().length() - 5));
        FileUtil.checkDirectory(PathUtil.getPath("EXPORT_PATH"));
        File projectFile = new File(path);
        if ((!projectFile.exists())) {
            LogUtil.warn("Project model file not found");
            return false;
        }

        // Read project file
        String data = FileUtil.read(path);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            projectModel = objectMapper.readValue(data, ProjectModel.class);
        } catch (Exception ex) {
            LogUtil.warn(ex.getMessage());
        }

        for (Table table: projectModel.getTables()) {
            for (Column column: table.getColumns()) {
                if (column.isPrimaryKey()) {
                    table.setPrimaryKey(column.getName());
                    break;
                }
            }
        }

        /*PathUtil.addPath("SPRING_BOOT_PROJECT_SOURCE_PATH", path + "/" + projectModel.getArtifactId() + "-backend" +
                "/src/main/java/" + (projectModel.getGroupId() +
                '.' + projectModel.getArtifactId()).replace('.', '/'));*/

        // this.frontendSourceBaseUrl = url + "/" + project.getArtifactId().getData() + "-frontend";
        return true;
    }


    @Override
    public boolean output() {
        return FileUtil.write(
                PathUtil.getPath("PROJECT_MODEL_PATH"),
                this.getProjectModel().toString()
        );
    }
}
