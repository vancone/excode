package com.vancone.excode.generator.entity.VO;

import lombok.Data;

/**
 * @author Tenton Lien
 * @date 3/14/2021
 */
@Data
public class SourceCodeVO {
    private String filename;
    private String format;
    private String content;
}
