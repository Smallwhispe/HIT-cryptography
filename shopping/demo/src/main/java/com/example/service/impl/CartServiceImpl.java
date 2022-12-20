package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.form.entity.Cart;
import com.example.form.SearchForm;
import com.example.mapper.CartMapper;
import com.example.service.CartService;
import com.example.vo.PageVO;
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
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    private CartMapper cartMapper;

    public PageVO list(Integer page, Integer size) {
        Page<Cart> cartPage = new Page<>(page, size);
        Page<Cart> resultPage = this.cartMapper.selectPage(cartPage, null);
        PageVO pageVO = new PageVO();
        pageVO.setTotal(resultPage.getTotal());
        pageVO.setData(resultPage.getRecords());
        return pageVO;
    }

    @Override
    public PageVO search(SearchForm searchForm) {
        //模糊查询+分页
        Page<Cart> cartPage = new Page<>(searchForm.getPage(), searchForm.getSize());
        Page<Cart> resultPage = null;
        if (searchForm.getPage().equals("")){
            resultPage = this.cartMapper.selectPage(cartPage,null);
        } else {
            QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
            queryWrapper.like(searchForm.getKey(),searchForm.getValue());
            resultPage = this.cartMapper.selectPage(cartPage,queryWrapper);
        }
        PageVO pageVO = new PageVO();
        pageVO.setTotal(resultPage.getTotal());
        pageVO.setData(resultPage.getRecords());
        return pageVO;
    }
}
