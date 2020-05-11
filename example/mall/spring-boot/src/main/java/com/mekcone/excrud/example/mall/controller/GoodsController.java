package com.mekcone.excrud.example.mall.controller;

import com.mekcone.excrud.example.mall.entity.Goods;
import com.mekcone.excrud.example.mall.service.GoodsService;
import com.github.pagehelper.PageInfo;
import com.mekcone.webplatform.common.model.Response;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping
    public Response create(@RequestBody Goods goods) {
        goodsService.create(goods);
        return Response.success();
    }

    @GetMapping("/{goodsId}")
    public Response retrieve(@PathVariable String goodsId) {
        List<Goods> goodsList = goodsService.retrieve(goodsId);
        return Response.success(goodsList);
    }

    @GetMapping
    public Response retrieveList(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        PageInfo<Goods> goodsList = goodsService.retrieveList(pageNo, pageSize);
        return Response.success(goodsList);
    }

    @PutMapping
    public Response update(@RequestBody Goods Goods) {
        goodsService.update(Goods);
        return Response.success();
    }

    @DeleteMapping("/{goodsId}")
    public Response delete(@PathVariable String goodsId) {
        goodsService.delete(goodsId);
        return Response.success();
    }
}
