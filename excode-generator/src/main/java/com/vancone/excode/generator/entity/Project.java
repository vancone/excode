/*
package com.vancone.excode.generator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

*/
/**
 * @author Tenton Lien
 * @since 2020/09/09
 *//*

@Data
@Table
@Entity
@ApiModel("Low-Code Project")
@JsonInclude(JsonInclude.Include.NON_NULL)
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Project {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    private String name;

    private String version;

    private String description;

    private String author;

    private String organization;

    @CreationTimestamp
    @Column(updatable = false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
}
*/
