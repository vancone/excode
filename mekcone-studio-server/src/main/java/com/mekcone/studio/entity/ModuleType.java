package com.mekcone.studio.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * Author: Tenton Lien
 * Date: 9/14/2020
 */

@Data
@Entity
@Table(name = "module_type")
public class ModuleType {

    @Id
    private String id;

    private String name;

    private String description;

    private String publisher;

}
