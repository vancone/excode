package com.vancone.excode.entity.microservice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vancone.excode.enums.OrmType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Data
@Entity
@Table(name = "microservice_spring_boot")
@JsonInclude(JsonInclude.Include.NON_NULL)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class SpringBootMicroservice extends Microservice {

    private String groupId;

    private String artifactId;

    private String version;

    private Integer serverPort;

    @Enumerated(EnumType.STRING)
    private OrmType ormType;

    @Column(updatable = false)
    private String creator;

    private String updater;

    @Column(updatable = false)
    @CreationTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
}
