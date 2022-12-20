package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.form.entity.User;
import com.example.form.RuleForm;
import com.example.form.SearchForm;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.vo.PageVO;
import com.example.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author admin
 * @since 2022-10-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

//  查询数据库
    @Autowired
    private UserMapper userMapper;
    @Override
    public ResultVO login(RuleForm ruleForm) {
//      判断用户名是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",ruleForm.getUsername());
        User user = this.userMapper.selectOne(queryWrapper);
        ResultVO resultVO = new ResultVO();
        if (user == null){
            resultVO.setCode(-1);
        }else {
//          判断密码是否错误
            if(!user.getPassword().equals(ruleForm.getPassword()))
                resultVO.setCode(-2);
            else {
                resultVO.setCode(0);
                resultVO.setData(user);
            }
        }
        return resultVO;
    }
    public PageVO list(Integer page, Integer size) {
        Page<User> userPage = new Page<>(page, size);
        Page<User> resultPage = this.userMapper.selectPage(userPage, null);
        PageVO pageVO = new PageVO();
        pageVO.setTotal(resultPage.getTotal());
        pageVO.setData(resultPage.getRecords());
        return pageVO;
    }

    @Override
    public PageVO search(SearchForm searchForm) {
        //模糊查询+分页
        Page<User> userPage = new Page<>(searchForm.getPage(), searchForm.getSize());
        Page<User> resultPage = null;
        if (searchForm.getPage().equals("")){
            resultPage = this.userMapper.selectPage(userPage,null);
        } else {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.like(searchForm.getKey(),searchForm.getValue());
            resultPage = this.userMapper.selectPage(userPage,queryWrapper);
        }
        PageVO pageVO = new PageVO();
        pageVO.setTotal(resultPage.getTotal());
        pageVO.setData(resultPage.getRecords());
        return pageVO;
    }
}

