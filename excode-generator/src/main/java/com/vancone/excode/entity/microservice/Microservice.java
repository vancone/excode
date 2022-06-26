package com.vancone.excode.entity.microservice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vancone.excode.entity.Project;
import com.vancone.excode.enums.MicroserviceType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Data
@MappedSuperclass
public class Microservice {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MicroserviceType type;

    private String description;

    @ManyToOne
    private Project project;

    @Column(updatable = false)
    private String creator;

    private String updater;

    @CreationTimestamp
    @Column(updatable = false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
}
