package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.form.entity.Shop;
import com.example.form.RuleForm;
import com.example.form.SearchForm;
import com.example.mapper.ShopMapper;
import com.example.service.ShopService;
import com.example.vo.PageVO;
import com.example.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2022-10-16
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public ResultVO login(RuleForm ruleForm) {
        //1、判断用户名是否存在
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", ruleForm.getUsername());
        Shop shop = this.shopMapper.selectOne(queryWrapper);
        ResultVO resultVO = new ResultVO();
        if(shop == null){
            resultVO.setCode(-1);
        } else {
            //2、判断密码是否正确
            if(!shop.getPassword().equals(ruleForm.getPassword())){
                resultVO.setCode(-2);
            } else {
                resultVO.setCode(0);
                resultVO.setData(shop);
            }
        }
        return resultVO;
    }
    public PageVO list(Integer page, Integer size) {
        Page<Shop> shopPage = new Page<>(page, size);
        Page<Shop> resultPage = this.shopMapper.selectPage(shopPage, null);
        PageVO pageVO = new PageVO();
        pageVO.setTotal(resultPage.getTotal());
        pageVO.setData(resultPage.getRecords());
        return pageVO;
    }

    @Override
    public PageVO search(SearchForm searchForm) {
        //模糊查询+分页
        Page<Shop> shopPage = new Page<>(searchForm.getPage(), searchForm.getSize());
        Page<Shop> resultPage = null;
        if (searchForm.getPage().equals("")){
            resultPage = this.shopMapper.selectPage(shopPage,null);
        } else {
            QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
            queryWrapper.like(searchForm.getKey(),searchForm.getValue());
            resultPage = this.shopMapper.selectPage(shopPage,queryWrapper);
        }
        PageVO pageVO = new PageVO();
        pageVO.setTotal(resultPage.getTotal());
        pageVO.setData(resultPage.getRecords());
        return pageVO;
    }
}
