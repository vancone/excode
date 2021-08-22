package com.vancone.excode.executor.entity;

import com.vancone.excode.executor.enums.ProgrammingLanguage;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Tenton Lien
 * @since 8/1/2021
 */
@Data
@Document
public class Segment {
    private String id;
    private String name;
    private ProgrammingLanguage type;
    private String content;
    private String creator;
    private String createdTime;
    private String updater;
    private String updatedTime;
}
