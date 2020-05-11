package com.mekcone.excrud.example.mall.service.impl;

import com.mekcone.excrud.example.mall.entity.Goods;
import com.mekcone.excrud.example.mall.mapper.GoodsMapper;
import com.mekcone.excrud.example.mall.service.GoodsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public void create(Goods goods) {
        goodsMapper.create(goods);
    }

    @Override
    public List<Goods> retrieve(String goodsId) {
        List<Goods> goodsList = goodsMapper.retrieve(goodsId);
        return goodsList;
    }

    @Override
    public List<Goods> retrieveList() {
        List<Goods> goodsList = goodsMapper.retrieveList();
        return goodsList;
    }

    @Override
    public PageInfo<Goods> retrieveList(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Goods> goodsList = retrieveList();
        PageInfo<Goods> pageInfo = new PageInfo<>(goodsList);
        return pageInfo;
    }

    @Override
    public void update(Goods goods) {
        goodsMapper.update(goods);
    }

    @Override
    public void delete(String goodsId) {
        goodsMapper.delete(goodsId);
    }
}
