package com.example.controller;

import com.example.form.entity.Goods;
import com.example.form.SearchForm;
import com.example.service.GoodsService;
import com.example.util.ResultVOUtil;
import com.example.vo.PageVO;
import com.example.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-10-16
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/save")//get请求不加注解，post请求加注解RequestBody
    public ResultVO save(@RequestBody Goods goods){
        boolean save = this.goodsService.save(goods);
        if(!save)return ResultVOUtil.fail();
        else return ResultVOUtil.success(goods);
    }

    @GetMapping("/list/{page}/{size}")
    public ResultVO list(@PathVariable("page") Integer page,@PathVariable("size") Integer size) {
        PageVO pageVO = this.goodsService.list(page, size);
        return ResultVOUtil.success(pageVO);
    }

    @GetMapping("/search")
    public ResultVO search(SearchForm searchForm){
        PageVO pageVO = this.goodsService.search(searchForm);
        return ResultVOUtil.success(pageVO);
    }

    @GetMapping("/findById/{id}")
    public ResultVO findById(@PathVariable("id") Integer id){
        Goods goods = this.goodsService.getById(id);
        return ResultVOUtil.success(goods);
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody Goods goods){
        boolean update = this.goodsService.updateById(goods);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResultVO deleteById(@PathVariable("id") Integer id){
        boolean remove = this.goodsService.removeById(id);
        if(!remove) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }
}

