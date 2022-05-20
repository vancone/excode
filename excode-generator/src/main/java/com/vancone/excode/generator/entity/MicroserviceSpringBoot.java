package com.vancone.excode.generator.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Data
@Entity
@Table
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class MicroserviceSpringBoot extends Microservice {

    private String groupId;

    private String artifactId;

    private String version;

    private Integer serverPort;

}
