package com.vancone.excode.generator.entity.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Tenton Lien
 * @date 3/13/2021
 */
@Data
public class OfficialDocument {

    private String id;

    private String title;

    private String author;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    private String content;
}
