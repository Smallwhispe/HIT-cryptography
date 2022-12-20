package com.example.controller;

import com.example.form.LoginMessage;
import com.example.form.RuleForm;
import com.example.form.SearchForm;
import com.example.form.ShopForm;
import com.example.form.entity.RSAED;
import com.example.form.entity.Shop;
import com.example.service.ShopService;
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
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/login")
    public ResultVO login(LoginMessage loginMessage) throws Exception {
        RuleForm ruleform = new RSAED().login(loginMessage);
        ResultVO resultVO = this.shopService.login(ruleform);
        return resultVO;
    }

    @PostMapping("/save")//get请求不加注解，post请求加注解RequestBody
    public ResultVO save(@RequestBody ShopForm shopForm) throws Exception {
        Shop shop = new RSAED().shop_register(shopForm);
        boolean save = this.shopService.save(shop);
        if(!save)return ResultVOUtil.fail();
        else return ResultVOUtil.success(shop);
    }

    @GetMapping("/list/{page}/{size}")
    public ResultVO list(@PathVariable("page") Integer page,@PathVariable("size") Integer size) {
        PageVO pageVO = this.shopService.list(page, size);
        return ResultVOUtil.success(pageVO);
    }

    @GetMapping("/search")
    public ResultVO search(SearchForm searchForm){
        PageVO pageVO = this.shopService.search(searchForm);
        return ResultVOUtil.success(pageVO);
    }

    @GetMapping("/findById/{id}")
    public ResultVO findById(@PathVariable("id") Integer id){
        Shop shop = this.shopService.getById(id);
        return ResultVOUtil.success(shop);
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody Shop shop){
        boolean update = this.shopService.updateById(shop);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResultVO deleteById(@PathVariable("id") Integer id){
        boolean remove = this.shopService.removeById(id);
        if(!remove) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }
}

