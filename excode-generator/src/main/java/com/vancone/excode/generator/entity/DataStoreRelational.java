package com.vancone.excode.generator.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Data
@Entity
@Table
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class DataStoreRelational extends DataStoreNew {

    @Column(name = "db")
    private String database;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "dataStoreRelational"
    )
    private List<DataStoreRelationalColumn> columns;

}
