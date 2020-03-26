package com.mekcone.excrud.service.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.enums.ErrorEnums;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.project.components.Column;
import com.mekcone.excrud.model.project.components.Database;
import com.mekcone.excrud.model.project.components.Export;
import com.mekcone.excrud.model.project.components.Table;
import com.mekcone.excrud.service.ProjectService;
import com.mekcone.excrud.service.SettingService;
import com.mekcone.excrud.service.SpringBootProjectService;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private Project project;

    @Autowired
    private SpringBootProjectService springBootProjectService;

    @Autowired
    private SettingService settingService;

    private final String projectFileName = "excrud.xml";

    @Override
    public void generate() {

        if (project.getExports().isEmpty()) {
            LogUtil.warn("No export options found");
        }
        for (Export export: project.getExports()) {
            if (export.getType().equals("spring-boot-project")) {
                springBootProjectService.generate();
            }
        }
    }

    @Override
    public Project getProject() {
        return this.project;
    }

    @Override
    public boolean load(String path) {
        File projectFile = new File(projectFileName);
        if ((!projectFile.exists())) {
            LogUtil.error(ErrorEnums.PROJECT_FILE_NOT_FOUND);
        }

        try {
            XmlMapper xmlMapper = new XmlMapper();
            project = xmlMapper.readValue(FileUtil.read(path), Project.class);
            LogUtil.info("Load project \"" + path + "\" complete");
        } catch (Exception ex) {
            LogUtil.error(ErrorEnums.PARSE_XML_FAILED);
            //LogUtil.error("Parse XML failed (" + ex.getMessage() + ")");
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

    @Override
    public boolean output() {
        return false;
    }
}
