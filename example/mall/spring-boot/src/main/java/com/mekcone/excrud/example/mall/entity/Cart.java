package com.mekcone.excrud.example.mall.entity;

import lombok.Data;

@Data
public class Cart {

    private String itemId;

    private String goodsId;

    private String userId;

    private int amount;
}
