package com.mekcone.excrud.example.mall.service;

import com.mekcone.excrud.example.mall.entity.Cart;
import com.github.pagehelper.PageInfo;
import java.util.List;

public interface CartService {

    void create(Cart cart);

    List<Cart> retrieve(String itemId);

    List<Cart> retrieveList();

    PageInfo<Cart> retrieveList(int pageNo, int pageSize);

    void update(Cart cart);

    void delete(String itemId);
}
