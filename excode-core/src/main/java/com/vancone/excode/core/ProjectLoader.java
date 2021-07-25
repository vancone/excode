package com.vancone.excode.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancone.excode.core.enums.ErrorEnum;
import com.vancone.excode.core.model.Project;
import com.vancone.excode.core.model.datasource.MysqlDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Tenton Lien
 * @date 7/24/2021
 */
@Slf4j
public class ProjectLoader {

    /**
     * Read JSON string
     */
    public static Project parse(String content) {
        Project project = new Project();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            project = mapper.readValue(content, Project.class);
            log.info("Load project {}:{} completed", project.getGroupId(), project.getArtifactId());
        } catch (Exception e) {
            log.error("{}: {}", ErrorEnum.PARSE_XML_FAILED, e.getMessage());
        }

        // Verify
        boolean result = verifyDataSource(project);
        if (!result) {
            return null;
        }

        return project;
    }

    /**
     * Read from file
     * @param path
     */
    public static void load(String path) {

    }

    private static boolean verifyDataSource(Project project) {
        Project.DataSource dataSource = project.getDatasource();
        if (dataSource == null) {
            log.error("Data source not configured.");
            return false;
        }

        MysqlDataSource mysql = dataSource.getMysql();
        if (mysql != null) {
            // Verify connection
            MysqlDataSource.Connection connection = mysql.getConnection();
            if (StringUtils.isBlank(connection.getHost()) || StringUtils.isBlank(connection.getDatabase())) {
                log.error("MySQL connection host or database not set.");
                return false;
            }

            if (connection.getPort() == 0) {
                connection.setPort(3306);
            }

            log.info("Verify MySQL Datasource: {} tables detected.", mysql.getTables().size());
            for (MysqlDataSource.Table table: project.getDatasource().getMysql().getTables()) {
                for (MysqlDataSource.Table.Column column: table.getColumns()) {
                    if (column.isPrimaryKey()) {
                        table.setPrimaryKeyName(column.getName());
                        break;
                    }
                }
            }
        }
        return true;
    }
}
