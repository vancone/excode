package com.vancone.excode.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vancone.excode.enums.TemplateType;
import lombok.Data;

/**
 * @author Tenton Lien
 * @since 2022/03/09
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Output {
    private TemplateType type;
    private String path;
    private Template template;
    private String content;

    public Output() {}

    public Output(TemplateType type, String path, Template template) {
        this.type = type;
        this.path = path;
        this.template = template;
    }

    public Output(TemplateType type, String path, String content) {
        this.type = type;
        this.path = path;
        this.content = content;
    }
}