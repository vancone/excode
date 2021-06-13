package com.vancone.excode.entity.DTO.data;

import lombok.Data;

import java.util.List;

/**
 * @author Tenton Lien
 * @date 6/13/2021
 */
@Data
public class DataNode {
    private String value;
    private String type;
    private List<DataNode> nodes;
}
