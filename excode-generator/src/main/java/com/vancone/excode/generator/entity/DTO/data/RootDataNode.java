package com.vancone.excode.generator.entity.DTO.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vancone.excode.generator.entity.DTO.data.source.DataSource;
import lombok.Data;
import org.springframework.data.annotation.Transient;

/**
 * @author Tenton Lien
 * @date 6/13/2021
 */
@Data
public class RootDataNode extends DataNode {

    @JsonIgnore
    private String dataSourceId;

    @Transient
    private DataSource dataSource;

    public RootDataNode() {
        root = true;
    }
}
