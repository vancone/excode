package com.vancone.excode.core;

import com.vancone.excode.core.constant.ModuleType;
import com.vancone.excode.core.enums.TemplateType;
import com.vancone.excode.core.generator.SpringBootGenerator;
import com.vancone.excode.core.model.Module;
import com.vancone.excode.core.model.Project;
import com.vancone.excode.core.model.Template;
import com.vancone.excode.core.model.datasource.MysqlDataSource;
import com.vancone.excode.core.util.FileUtil;
import com.vancone.excode.core.util.SqlUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 * @date 7/24/2021
 */
@Slf4j
public class ProjectWriter {

    private final String genLocation = "/opt/excode/gen" + File.separator;

    private Project project;

    private String rootDirectory;

    private List<Output> outputs = new ArrayList<>();

    public ProjectWriter(Project project) {
        this.project = project;
        rootDirectory = genLocation + project.getGroupId() + "." + project.getArtifactId() + "-" + project.getVersion() + File.separator;
//        rootDirectory = genLocation + project.getGroupId() + "." + project.getArtifactId() + "-" + project.getVersion() + "-" + System.currentTimeMillis() + File.separator;
    }

    public Project getProject() {
        return project;
    }

    public void addOutput(TemplateType type, String target, String content) {
        outputs.add(new Output(type, target, content));
    }

    public void addOutput(TemplateType type, String target, Template template) {
        outputs.add(new Output(type, target, template));
    }

    public List<Output> getOutputsByType(TemplateType type) {
        List<Output> results = new ArrayList<>();
        for (Output output: outputs) {
            if (output.getType().equals(type)) {
                results.add(output);
            }
        }
        return results;
    }

    public void write() {
        if (project.getModules().isEmpty()) {
            log.error("No valid module found");
            return;
        }

        // Data source
        MysqlDataSource mysql = project.getDatasource().getMysql();
        if (mysql != null && mysql.isSqlGen()) {
            String sql = SqlUtil.createDatabase(mysql) + "\n\n";

            for (MysqlDataSource.Table table: mysql.getTables()) {
                sql += SqlUtil.createTable(table) + "\n\n";
            }

            addOutput(TemplateType.SQL, "create.sql", sql);
        }

        for (Module module: project.getModules()) {
            if (module.getType().equals(ModuleType.SPRING_BOOT)) {
                log.info("Generate module [{}]", module.getType());
                SpringBootGenerator.generate(module, this);
            }
        }

        // Write to disk
        for (Output output: outputs) {
            String content = output.getTemplate() == null ? output.getContent() : output.getTemplate().getContent();
            FileUtil.write(rootDirectory + output.getPath(), content);
        }
    }

    @Data
    public static class Output {
        private TemplateType type;
        private String path;
        private Template template;
        private String content;

        public Output(TemplateType type, String path, Template template) {
            this.type = type;
            this.path = path;
            this.template = template;
        }

        public Output(TemplateType type, String path, String content) {
            this.type = type;
            this.path = path;
            this.content = content;
        }
    }
}
