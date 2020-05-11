package com.mekcone.excrud.example.mall.entity;

import lombok.Data;

@Data
public class Goods {

    private int id;

    private String name;

    private String catalogueId;

    private String price;

    private String description;
}
