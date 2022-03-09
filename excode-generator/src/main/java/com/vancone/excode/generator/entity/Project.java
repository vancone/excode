package com.vancone.excode.generator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vancone.excode.generator.enums.DataCarrier;
import com.vancone.excode.generator.enums.OrmType;
import com.vancone.excode.generator.entity.datasource.MysqlDataSource;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tenton Lien
 * @since 2021/7/24
 */
@Data
@Document("project")
public class Project {
    @Id
    private String id;
    private String version;
    private List<String> languages;
    private String name;
    private String author;
    private String description;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    private DataAccess dataAccess;

    @Data
    public static class DataAccess {

        private Solution solution;

        @Transient
        private List<DataStore> dataStores;

        @Data
        public static class Solution {
            private String type;
            private JavaSpringBoot javaSpringBoot;

            @Data
            public static class JavaSpringBoot {
                private String groupId;
                private String artifactId;
                private String version;
                private String name;
                private String description;
                private OrmType ormType;
                private Integer serverPort;
                private Boolean postmanCollection;
                private List<Extension> extensions;

                @Transient
                private String packagePath;

                @Data
                public static class Extension {
                    private String id;
                    private boolean enable;
                    private Map<String, String> properties;

                    @JsonIgnore
                    public String getProperty(String id) {
                        return properties.get(id);
                    }
                }
            }
        }


        public List<DataStore> getDataStoreByCarrier(DataCarrier carrier) {
            List<DataStore> stores = new ArrayList<>();
            for (DataStore store: this.dataStores) {
                if (store.getCarrier() == carrier) {
                    stores.add(store);
                }
            }
            return stores;
        }
    }

    @Data
    public static class DataSource {
        private MysqlDataSource mysql;
    }
}
