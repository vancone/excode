package com.mekcone.excrud.service.impl;

import cn.hutool.crypto.asymmetric.RSA;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.project.components.Column;
import com.mekcone.excrud.model.project.components.Table;
import com.mekcone.excrud.service.ProjectService;
import com.mekcone.excrud.service.SettingService;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.util.PathUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private Project project;

    @Autowired
    private SettingService settingService;

    @Value("${project.file.extention}")
    private String projectFileExtention;

    @Override
    public Project getProject() {
        return this.project;
    }

    @Override
    public boolean load() {
        String projectModelPath = PathUtil.getProjectModelPath();
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
        PathUtil.addPath("EXPORT_PATH", file.getParent() + "/" + file.getName().substring(0, file.getName().length() - (projectFileExtention.length() + 1)));
        FileUtil.checkDirectory(PathUtil.getPath("EXPORT_PATH"));
        File projectFile = new File(path);
        if ((!projectFile.exists())) {
            LogUtil.warn("Project model file not found");
            return false;
        }

        // Read project file
        String data = FileUtil.read(path);

        try {
            XmlMapper xmlMapper = new XmlMapper();
            project = xmlMapper.readValue(data, Project.class);
            LogUtil.debug(project.toString());
//            ObjectMapper objectMapper = new ObjectMapper();
//            project = objectMapper.readValue(data, Project.class);
        } catch (Exception ex) {
            LogUtil.warn(ex.getMessage());
        }
        return false;
//        for (Table table: project.getDatabases().get(0).getTables()) {
//            for (Column column: table.getColumns()) {
//                if (column.isPrimaryKey()) {
//                    table.setPrimaryKey(column.getName());
//                    break;
//                }
//            }
//        }
//
//        // TEST: Encrypt with RSA public key
//        RSA rsaTest = new RSA(null, settingService.getSettings().getDefaultRsaPublicKeyBytes());
////        for (Dependency dependency: project.getDependencies()) {
////            if (dependency.getProperties() != null) {
////                for (Property property: dependency.getProperties()) {
////                    byte[] encrypt = rsaTest.encrypt(StrUtil.bytes(property.getValue(), CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
////                    LogUtil.debug(property.getValue() + ": RSA(" + Base64.encode(encrypt) + ")");
////                }
////            }
////        }
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
//
//        ChangeListener modifiedStateChangeListener = new ChangeListener() {
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                project.modifiedStateProperty().setValue(
//                        project.modifiedStateProperty().getValue() + 1
//                );
//                output();
//            }
//        };
//
//        project.groupIdProperty().addListener(modifiedStateChangeListener);
//        project.artifactIdProperty().addListener(modifiedStateChangeListener);
//        project.versionProperty().addListener(modifiedStateChangeListener);
//        project.exportsProperty().addListener(modifiedStateChangeListener);
//
//        // Update projectTreeView
//        //projectTreeView.update();
//
//        return true;
    }

    @Override
    public boolean output() {
        return false;
        /*return FileUtil.write(
                PathUtil.getProjectModelPath(),
                this.getProject().toString()
        );*/
    }
}
