package com.vancone.devops.entity.VO;

import lombok.Data;

import java.util.List;

/**
 * @author Tenton Lien
 * @date 3/14/2021
 */
@Data
public class DirectoryVO {
    private String name;
    private String format;
    private String content;
    private List<SourceCodeVO> file;
}
