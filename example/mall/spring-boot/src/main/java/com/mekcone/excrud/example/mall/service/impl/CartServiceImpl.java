package com.mekcone.excrud.example.mall.service.impl;

import com.mekcone.excrud.example.mall.entity.Cart;
import com.mekcone.excrud.example.mall.mapper.CartMapper;
import com.mekcone.excrud.example.mall.service.CartService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Override
    public void create(Cart cart) {
        cartMapper.create(cart);
    }

    @Override
    public List<Cart> retrieve(String itemId) {
        List<Cart> cartList = cartMapper.retrieve(itemId);
        return cartList;
    }

    @Override
    public List<Cart> retrieveList() {
        List<Cart> cartList = cartMapper.retrieveList();
        return cartList;
    }

    @Override
    public PageInfo<Cart> retrieveList(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<Cart> cartList = retrieveList();
        PageInfo<Cart> pageInfo = new PageInfo<>(cartList);
        return pageInfo;
    }

    @Override
    public void update(Cart cart) {
        cartMapper.update(cart);
    }

    @Override
    public void delete(String itemId) {
        cartMapper.delete(itemId);
    }
}
