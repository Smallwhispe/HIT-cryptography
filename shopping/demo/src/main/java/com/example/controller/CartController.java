package com.example.controller;

import com.example.form.ConfirmForm;
import com.example.form.SearchForm;
import com.example.form.entity.Cart;
import com.example.form.entity.RSAED;
import com.example.form.entity.SHA256Hasher;
import com.example.service.CartService;
import com.example.util.ResultVOUtil;
import com.example.vo.PageVO;
import com.example.vo.ResultVO;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-10-16
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

//    @PostMapping("/save")//get请求不加注解，post请求加注解RequestBody
//    public ResultVO save(@RequestBody Cart cart){
//
//        boolean save = this.cartService.save(cart);
//        if(!save)return ResultVOUtil.fail();
//        else return ResultVOUtil.success(cart);
//    }

    @PostMapping("/save")//get请求不加注解，post请求加注解RequestBody
    public void save(@RequestBody Cart cart1) throws IOException {
        String order = cart1.getShop()+cart1.getName()+cart1.getStyle();
        SHA256Hasher sha256Hasher = new SHA256Hasher();
        String orderhash = sha256Hasher.SHA256(order);
        File file = new File("order/" + orderhash + ".txt");
        if(!file.exists()) {
            file.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
        }
        String cart = cart1.getName().toString()+
                ";"+cart1.getPrice().toString()+
                ";"+cart1.getStyle().toString()+
                ";"+cart1.getShop().toString()+
                ";"+cart1.getShopaccount().toString()+
                ";"+cart1.getAccount().toString()+
                ";"+cart1.getPassword().toString();
        FileUtils.write(file, cart, StandardCharsets.UTF_8);
    }

    @GetMapping("/list/{page}/{size}")
    public ResultVO list(@PathVariable("page") Integer page,@PathVariable("size") Integer size) {
        PageVO pageVO = this.cartService.list(page, size);
        return ResultVOUtil.success(pageVO);
    }

    @GetMapping("/search")
    public ResultVO search(SearchForm searchForm){
        PageVO pageVO = this.cartService.search(searchForm);
        return ResultVOUtil.success(pageVO);
    }

    @GetMapping("/findById/{id}")
    public ResultVO findById(@PathVariable("id") Integer id){
        Cart cart = this.cartService.getById(id);
        return ResultVOUtil.success(cart);
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody Cart cart){
        boolean update = this.cartService.updateById(cart);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResultVO deleteById(@PathVariable("id") Integer id){
        boolean remove = this.cartService.removeById(id);
        if(!remove) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    @PostMapping("/confirm")
    public ResultVO confirm(@RequestBody ConfirmForm confirmForm) throws Exception {
        ConfirmForm confirmForm1 = new RSAED().confirm(confirmForm);
        if (confirmForm1.getCode().equals("0")){
            String path = "order/" +confirmForm1.getOrderhash()+ ".txt";
            Path readingpath = Paths.get(path);
            String cartcode = Files.readString(readingpath);
            String[] carts = cartcode.split(";");
            Cart cart = new Cart();
            cart.setName(carts[0]);
            cart.setPrice(carts[1]);
            cart.setStyle(carts[2]);
            cart.setShop(carts[3]);
            cart.setShopaccount(carts[4]);
            cart.setAccount(carts[5]);
            cart.setPassword(carts[6]);
            boolean save = this.cartService.save(cart);
            if(!save){
                System.out.println(confirmForm1.getCode()+"fail!!!!!!!!!!!!!!!!!!!!!!!");
                return ResultVOUtil.fail();
            }
            else {
                System.out.println(confirmForm1.getCode()+"success!!!!!!!!!!!!!!!!!!!!!!!");
                return ResultVOUtil.success(cart);
            }
        }
        else {
            System.out.println(confirmForm1.getCode()+"fail2!!!!!!!!!!!!!!!!!!!!!!!");
            return ResultVOUtil.fail2();
        }
    }
}

