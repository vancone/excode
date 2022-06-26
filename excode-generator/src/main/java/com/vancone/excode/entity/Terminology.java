package com.vancone.excode.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Tenton Lien
 * @since 2022/03/17
 */
@Data
@Document("terminology")
public class Terminology {
    @Id
    private String id;
    private String key;
    private String value;
    private String description;
    private String language;
}
