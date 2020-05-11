package com.mekcone.excrud.example.mall.controller;

import com.mekcone.excrud.example.mall.entity.Cart;
import com.mekcone.excrud.example.mall.service.CartService;
import com.github.pagehelper.PageInfo;
import com.mekcone.webplatform.common.model.Response;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public Response create(@RequestBody Cart cart) {
        cartService.create(cart);
        return Response.success();
    }

    @GetMapping("/{itemId}")
    public Response retrieve(@PathVariable String itemId) {
        List<Cart> cartList = cartService.retrieve(itemId);
        return Response.success(cartList);
    }

    @GetMapping
    public Response retrieveList(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        PageInfo<Cart> cartList = cartService.retrieveList(pageNo, pageSize);
        return Response.success(cartList);
    }

    @PutMapping
    public Response update(@RequestBody Cart Cart) {
        cartService.update(Cart);
        return Response.success();
    }

    @DeleteMapping("/{itemId}")
    public Response delete(@PathVariable String itemId) {
        cartService.delete(itemId);
        return Response.success();
    }
}
