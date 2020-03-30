package com.mekcone.excrud.loader;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.constant.ExportTypes;
import com.mekcone.excrud.enums.ErrorEnums;
import com.mekcone.excrud.generator.ApiDocumentGenerator;
import com.mekcone.excrud.generator.SpringBootBackendGenerator;
import com.mekcone.excrud.loader.model.Project;
import com.mekcone.excrud.loader.model.components.Export;
import com.mekcone.excrud.loader.model.data.Column;
import com.mekcone.excrud.loader.model.data.Database;
import com.mekcone.excrud.loader.model.data.Table;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import lombok.Getter;

import java.io.File;
import java.util.List;

public class ProjectLoader {
    @Getter
    private Project project;

    private final String projectFileName = "excrud.xml";

    public void generate() {

        if (project.getExports().isEmpty()) {
            LogUtil.warn("No export options found");
        }
//        for (Exports exports : project.getExports()) {
//            if (exports.getType().equals("spring-boot-project")) {
//                SpringBootProjectGenerator springBootProjectGenerator = new SpringBootProjectGenerator(project);
//                springBootProjectGenerator.generate();
//            }
//        }

        // API Document
        ApiDocumentGenerator apiDocumentGenerator = new ApiDocumentGenerator(project);
        System.exit(-1);

        if (project.getExports() != null && !project.getExports().isEmpty()) {
            for (Export export: project.getExports()) {
                LogUtil.title(export.getType());
                switch (export.getType()) {
                    case ExportTypes.SPRING_BOOT_BACKEND:
                        SpringBootBackendGenerator springBootBackendGenerator = new SpringBootBackendGenerator(project, export);
                        springBootBackendGenerator.generate();
                        break;
                    case ExportTypes.ENTERPRISE_OFFICIAL_WEBSITE:
                        break;
                    default:
                        LogUtil.warn("Unsupported export type \"" + export.getType() + "\"");
                }
            }
        }
    }

    public boolean load(String path) {
        File projectFile = new File(projectFileName);
        if ((!projectFile.exists())) {
            LogUtil.error(ErrorEnums.PROJECT_FILE_NOT_FOUND);
        }

        try {
            XmlMapper xmlMapper = new XmlMapper();
            project = xmlMapper.readValue(FileUtil.read(path), Project.class);
            LogUtil.info("Load project " + project.getGroupId() + "." + project.getArtifactId() + " completed");
        } catch (Exception ex) {
            //LogUtil.error(ErrorEnums.PARSE_XML_FAILED);
            LogUtil.error(-1, "Parse XML failed (" + ex.getMessage() + ")");
        }

        List<Database> databases = project.getDatabases();
        if (databases == null || databases.isEmpty()) {
            LogUtil.error(ErrorEnums.DATABASE_UNDEFINED);
        }

        int tableAmount = 0;
        for (Database database: databases) {
            if (database == null) {
                continue;
            }
            for (Table table: database.getTables()) {
                if (table != null) {
                    tableAmount ++;
                }
            }
        }
        if (tableAmount == 0) {
            LogUtil.error(ErrorEnums.TABLE_UNDEFINED);
        } else {
            LogUtil.info(databases.size() + " database(s), " + tableAmount + " table(s) detected");
        }

        for (Table table: project.getDatabases().get(0).getTables()) {
            for (Column column: table.getColumns()) {
                if (column.isPrimaryKey()) {
                    table.setPrimaryKey(column.getName());
                    break;
                }
            }
        }

        return true;
//
//        // TEST: Encrypt with RSA public key
//        RSA rsaTest = new RSA(null, settingService.getSettings().getDefaultRsaPublicKeyBytes());
//        for (Dependency dependency: project.getDependencies()) {
//            if (dependency.getProperties() != null) {
//                for (Property property: dependency.getProperties()) {
//                    byte[] encrypt = rsaTest.encrypt(StrUtil.bytes(property.getValue(), CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
//                    LogUtil.debug(property.getValue() + ": RSA(" + Base64.encode(encrypt) + ")");
//                }
//            }
//        }
//
//        String rsaPublicKey = project.getRsaPublicKey();
//        if (rsaPublicKey != null) {
//            byte[] rsaPrivateKey = settingService.getSettings().getRsaPrivateKeyBytes(rsaPublicKey);
//            if (rsaPrivateKey != null) {
//                /*for (Dependency dependency: project.getDependencies()) {
//                    if (dependency.getProperties() != null) {
//                        for (Property property: dependency.getProperties()) {
//                            if (property.getValue().matches("RSA\\(.*\\)")) {
//                                RSA rsa = new RSA(rsaPrivateKey, null);
//                                // LogUtil.error(property.getValue().substring(4, property.getValue().length() - 2));
//                                byte[] propertyBytes = Base64.decode(property.getValue().substring(4, property.getValue().length() - 1));
//                                byte[] decrypt = rsa.decrypt(propertyBytes, KeyType.PrivateKey);
//                                property.setValue(StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));
//                            }
//                        }
//                    }
//                }*/
//            } else {
//                LogUtil.warn("No RSA key pair matched");
//            }
//        }
    }


    public boolean output() {
        return false;
    }
}
