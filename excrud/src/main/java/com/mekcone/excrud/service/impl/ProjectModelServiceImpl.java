package com.mekcone.excrud.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.project.components.Column;
import com.mekcone.excrud.model.project.components.Dependency;
import com.mekcone.excrud.model.project.components.Property;
import com.mekcone.excrud.model.project.components.Table;
import com.mekcone.excrud.service.ProjectModelService;
import com.mekcone.excrud.service.SettingService;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.util.PathUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ProjectModelServiceImpl implements ProjectModelService {
    @Autowired
    private Project project;

//    @Autowired
//    private ProjectTreeView projectTreeView;

    @Autowired
    private SettingService settingService;

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
            project = objectMapper.readValue(data, Project.class);
        } catch (Exception ex) {
            LogUtil.warn(ex.getMessage());
        }

        for (Table table: project.getDatabases().get(0).getTables()) {
            for (Column column: table.getColumns()) {
                if (column.isPrimaryKey()) {
                    table.setPrimaryKey(column.getName());
                    break;
                }
            }
        }

        // TEST: Encrypt with RSA public key
        RSA rsaTest = new RSA(null, settingService.getSettingModel().getDefaultRsaPublicKeyBytes());
//        for (Dependency dependency: project.getDependencies()) {
//            if (dependency.getProperties() != null) {
//                for (Property property: dependency.getProperties()) {
//                    byte[] encrypt = rsaTest.encrypt(StrUtil.bytes(property.getValue(), CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
//                    LogUtil.debug(property.getValue() + ": RSA(" + Base64.encode(encrypt) + ")");
//                }
//            }
//        }

        String rsaPublicKey = project.getRsaPublicKey();
        if (rsaPublicKey != null) {
            byte[] rsaPrivateKey = settingService.getSettingModel().getRsaPrivateKeyBytes(rsaPublicKey);
            if (rsaPrivateKey != null) {
                /*for (Dependency dependency: project.getDependencies()) {
                    if (dependency.getProperties() != null) {
                        for (Property property: dependency.getProperties()) {
                            if (property.getValue().matches("RSA\\(.*\\)")) {
                                RSA rsa = new RSA(rsaPrivateKey, null);
                                // LogUtil.error(property.getValue().substring(4, property.getValue().length() - 2));
                                byte[] propertyBytes = Base64.decode(property.getValue().substring(4, property.getValue().length() - 1));
                                byte[] decrypt = rsa.decrypt(propertyBytes, KeyType.PrivateKey);
                                property.setValue(StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));
                            }
                        }
                    }
                }*/
            } else {
                LogUtil.warn("No RSA key pair matched");
            }
        }

        ChangeListener modifiedStateChangeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                project.modifiedStateProperty().setValue(
                        project.modifiedStateProperty().getValue() + 1
                );
                output();
            }
        };

        project.groupIdProperty().addListener(modifiedStateChangeListener);
        project.artifactIdProperty().addListener(modifiedStateChangeListener);
        project.versionProperty().addListener(modifiedStateChangeListener);
        project.exportsProperty().addListener(modifiedStateChangeListener);

        // Update projectTreeView
        //projectTreeView.update();

        return true;
    }

    @Override
    public boolean output() {
        return FileUtil.write(
                PathUtil.getProjectModelPath(),
                this.getProject().toString()
        );
    }
}
