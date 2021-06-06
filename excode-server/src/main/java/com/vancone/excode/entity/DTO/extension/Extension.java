package com.vancone.excode.entity.DTO.extension;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author Tenton Lien
 * @date 6/6/2021
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Extension {
    private String name;
    private Boolean enable = true;
}
