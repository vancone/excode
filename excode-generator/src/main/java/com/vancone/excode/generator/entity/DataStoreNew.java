package com.vancone.excode.generator.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@MappedSuperclass
public class DataStoreNew {

    @Id
    @Column(length = 36)
    private String id;

    private String name;

    @ManyToOne
    private ProjectNew project;
}
