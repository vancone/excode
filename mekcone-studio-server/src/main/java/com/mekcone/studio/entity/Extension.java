package com.mekcone.studio.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/*
 * Author: Tenton Lien
 * Date: 9/14/2020
 */

@Data
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "extension")
public class Extension {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    private String type;

    private String name;

    @ManyToOne
    private Module module;


}
