package com.vancone.excode.executor.entity;

import com.vancone.excode.executor.enums.ProgramType;
import lombok.Data;

/**
 * @author Tenton Lien
 * @since 8/1/2021
 */
@Data
public class Program {
    private String id;
    private String name;
    private ProgramType type;
    private String content;
}
