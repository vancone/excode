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
public class ModuleType {

    private String id;

    private Map<String, String> name;

    private String description;

    private String publisher;
}
