package com.vancone.excode.generator.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Data
@Entity
@Table
public class DataStoreSql extends DataStoreNew {

    @Column(name = "db")
    private String database;

}
