package com.vancone.excode.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@Table(name = "project")
@JsonInclude(JsonInclude.Include.NON_NULL)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Project {
    @Id
    @Column(length = 36)
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    private String name;

    private String owner;

    private String description;

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
