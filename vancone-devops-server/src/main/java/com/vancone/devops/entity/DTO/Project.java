package com.vancone.devops.entity.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author Tenton Lien
 * @date 9/9/2020
 */
@Data
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String version;

    private String organization;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    @CreationTimestamp
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private Date modifiedTime;

    @JsonManagedReference
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Module> modules;
}
