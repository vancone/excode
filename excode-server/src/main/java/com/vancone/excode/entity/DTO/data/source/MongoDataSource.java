package com.vancone.excode.entity.DTO.data.source;

import lombok.Data;

/**
 * @author Tenton Lien
 * @date 6/13/2021
 */
@Data
public class MongoDataSource extends DataSource {
    private String authDatabase;
}
