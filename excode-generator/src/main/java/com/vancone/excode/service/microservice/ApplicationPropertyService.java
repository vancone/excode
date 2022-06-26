package com.vancone.excode.service.microservice;

import cn.hutool.core.lang.Pair;
import com.vancone.excode.entity.Output;
import com.vancone.excode.entity.microservice.ApplicationProperty;
import com.vancone.excode.entity.microservice.SpringBootMicroservice;
import com.vancone.excode.enums.SpringProfile;
import com.vancone.excode.enums.TemplateType;
import com.vancone.excode.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

/**
 * @author Tenton Lien
 */
@Slf4j
@Service
public class ApplicationPropertyService {

    public Output generate(SpringProfile profile, SpringBootMicroservice microservice) {
        ApplicationProperty properties = new ApplicationProperty();

        ApplicationProperty.PropertyGroup basicGroup= properties.createPropertyGroup();
        basicGroup.add("spring.application.name", microservice.getArtifactId());
        basicGroup.add("server.port", microservice.getServerPort() == null ? "8080" : microservice.getServerPort().toString());

        // MySQL
//        if (!project.getDataAccess().getDataStoreByCarrier(DataCarrier.MYSQL).isEmpty()) {
//            DataStore.Connection connection = project.getDataAccess().getDataStoreByCarrier(DataCarrier.MYSQL).get(0).getConnection();
//            String datasourceUrl = "jdbc:mysql://" + connection.getHost() + ":" + connection.getPort() + "/" + connection.getDatabase();
//            datasourceUrl += connection.getTimezone() != null ? "?serverTimezone=" + connection.getTimezone() : "";
//            parser.add("spring.datasource.url", datasourceUrl);
//            parser.add("spring.datasource.username", connection.getUsername());
//            parser.add("spring.datasource.password", connection.getPassword());
//        }

        String outputContent = toProperties(properties);
        return new Output(TemplateType.SPRING_BOOT_PROPERTIES,
                microservice.getName() + "/src/main/resources/application-" + profile.name().toLowerCase() + ".properties",
                outputContent);
    }

    private String toProperties(ApplicationProperty properties) {
        String outputContent = "";
        for (ApplicationProperty.PropertyGroup propertyGroup: properties.getPropertyGroups()) {
            if (propertyGroup.getComment() != null) {
                outputContent += propertyGroup.getComment() + "\n";
            }
            for (Map.Entry item: propertyGroup.getItems().entrySet()) {
                outputContent +=  item.getKey() + "=" + item.getValue() + "\n";
            }
            outputContent += "\n";
        }
        return outputContent;
    }


    public ApplicationProperty parse(String propertiesText) {
        ApplicationProperty properties = new ApplicationProperty();
        properties.setName("unnamed");
        String[] propertiesArray = propertiesText.split("\n");
        for (String property: propertiesArray) {
            String[] keyAndValue = property.split("=");
            if (keyAndValue.length == 1) {
                continue;
            } else if (keyAndValue.length == 2) {
                String key = keyAndValue[0].trim();
                String value = keyAndValue[1].trim();
                properties.add(key, value);
            } else if (keyAndValue.length > 2) {
                String key = keyAndValue[0].trim();
                String value = "";
                for (int i = 1; i < keyAndValue.length; i ++) {
                    value += keyAndValue[i];
                    if (i + 1 != keyAndValue.length) {
                        value += "=";
                    }
                }
                properties.add(key, value);
            }
        }
        return properties;
    }

    public ApplicationProperty readFrom(String url) {
        String content = FileUtil.read(url);
        if (StringUtils.isNotBlank(content)) {
            String fileName = new File(url).toPath().getFileName().toString();
            ApplicationProperty properties = parse(content);
            properties.setName(fileName.substring(0, fileName.lastIndexOf(".")));
            return properties;
        }
        return null;
    }
}
