package com.vancone.excode.entity.DTO.data;

import com.vancone.excode.entity.DTO.data.source.DataSource;
import lombok.Data;

/**
 * @author Tenton Lien
 * @date 6/13/2021
 */
@Data
public class DataObject extends DataNode {
    private DataSource dataSource;
}
