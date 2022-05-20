package com.vancone.excode.generator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vancone.excode.generator.enums.DataCarrier;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Tenton Lien
 * @since 2021/06/13
 */
@Data
@Entity
@Table
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class DataSource {
    @Id
    @Column(length = 36)
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String name;

    @Enumerated(EnumType.STRING)
    private DataCarrier type;

    private String host;

    private Integer port;

    private String username;

    private String password;

    private Boolean auth;

    @Column(name = "db")
    private String database;

    @CreationTimestamp
    @Column(updatable = false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
}
