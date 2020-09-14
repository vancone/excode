package com.mekcone.studio.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

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

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;
}
