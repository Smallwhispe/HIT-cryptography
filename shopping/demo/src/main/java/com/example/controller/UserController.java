package com.example.controller;


import com.example.form.LoginMessage;
import com.example.form.RuleForm;
import com.example.form.SearchForm;
import com.example.form.UserForm;
import com.example.form.entity.User;
import com.example.service.UserService;
import com.example.util.ResultVOUtil;
import com.example.vo.PageVO;
import com.example.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.form.entity.RSAED;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author admin
 * @since 2022-10-16
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserService userService;

    @GetMapping("/login")
    public ResultVO login(LoginMessage loginMessage) throws Exception {
        RuleForm ruleform = new RSAED().login(loginMessage);
        ResultVO resultVO = this.userService.login(ruleform);
        return resultVO;
    }

    @PostMapping("/save")//get请求不加注解，post请求加注解RequestBody
    public ResultVO save(@RequestBody UserForm userForm) throws Exception {
        User user = new RSAED().user_register(userForm);
        boolean save = this.userService.save(user);
        if(!save)return ResultVOUtil.fail();
        else return ResultVOUtil.success(user);
    }

    @GetMapping("/list/{page}/{size}")
    public ResultVO list(@PathVariable("page") Integer page,@PathVariable("size") Integer size) {
        PageVO pageVO = this.userService.list(page, size);
        return ResultVOUtil.success(pageVO);
    }

    @GetMapping("/search")
    public ResultVO search(SearchForm searchForm){
        PageVO pageVO = this.userService.search(searchForm);
        return ResultVOUtil.success(pageVO);
    }

    @GetMapping("/findById/{id}")
    public ResultVO findById(@PathVariable("id") Integer id){
        User user = this.userService.getById(id);
        return ResultVOUtil.success(user);
    }

    @PutMapping("/update")
    public ResultVO update(@RequestBody User user){
        boolean update = this.userService.updateById(user);
        if(!update) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResultVO deleteById(@PathVariable("id") Integer id){
        boolean remove = this.userService.removeById(id);
        if(!remove) return ResultVOUtil.fail();
        return ResultVOUtil.success(null);
    }
}

