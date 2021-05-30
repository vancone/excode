package com.vancone.devops.entity.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author Tenton Lien
 * @date 9/14/2020
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Extension {
    private String id;

    private String type;

    private String name;
}
