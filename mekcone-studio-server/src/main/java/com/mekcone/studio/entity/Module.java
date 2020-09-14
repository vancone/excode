package com.mekcone.studio.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "module")
public class Module {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;

    private String type;

    private String name;

    @Transient
    private List<String> extensions;
}
