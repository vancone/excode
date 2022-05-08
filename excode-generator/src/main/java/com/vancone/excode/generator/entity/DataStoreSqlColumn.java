package com.vancone.excode.generator.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Data
@Entity
@Table
public class DataStoreSqlColumn {

    @Id
    @Column(length = 36)
    private String id;

    private String type;

    private String length;

    private String comment;

    private Boolean primaryKey;

    private Boolean foreignKey;

    @ManyToOne
    private DataStoreSql dataStoreSql;

}
