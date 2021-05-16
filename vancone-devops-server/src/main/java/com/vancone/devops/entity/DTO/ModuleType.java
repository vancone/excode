package com.vancone.devops.entity.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * @author Tenton Lien
 * @date 9/14/2020
 */
@Data
@Entity
@Table(name = "module_type")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ModuleType {

    @Id
    private String id;

    @Type(type = "json")
    @Column(columnDefinition = "LONGTEXT")
    private Map<String, String> name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String publisher;

    @JsonIgnore
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Module> module;

}
