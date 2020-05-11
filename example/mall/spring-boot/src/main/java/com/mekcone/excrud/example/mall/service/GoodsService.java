package com.mekcone.excrud.example.mall.service;

import com.mekcone.excrud.example.mall.entity.Goods;
import com.github.pagehelper.PageInfo;
import java.util.List;

public interface GoodsService {

    void create(Goods goods);

    List<Goods> retrieve(String goodsId);

    List<Goods> retrieveList();

    PageInfo<Goods> retrieveList(int pageNo, int pageSize);

    void update(Goods goods);

    void delete(String goodsId);
}
