package com.vancone.excode.generator.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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

    @ManyToOne
    private DataSource dataSource;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "dataStoreRelational"
    )
    private List<DataStoreRelationalColumn> columns;

}
