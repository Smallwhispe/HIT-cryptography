package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.form.entity.Goods;
import com.example.form.SearchForm;
import com.example.mapper.GoodsMapper;
import com.example.service.GoodsService;
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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    public PageVO list(Integer page, Integer size) {
        Page<Goods> goodsPage = new Page<>(page, size);
        Page<Goods> resultPage = this.goodsMapper.selectPage(goodsPage, null);
        PageVO pageVO = new PageVO();
        pageVO.setTotal(resultPage.getTotal());
        pageVO.setData(resultPage.getRecords());
        return pageVO;
    }

    @Override
    public PageVO search(SearchForm searchForm) {
        //模糊查询+分页
        Page<Goods> goodsPage = new Page<>(searchForm.getPage(), searchForm.getSize());
        Page<Goods> resultPage = null;
        if (searchForm.getPage().equals("")){
            resultPage = this.goodsMapper.selectPage(goodsPage,null);
        } else {
            QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
            queryWrapper.like(searchForm.getKey(),searchForm.getValue());
            resultPage = this.goodsMapper.selectPage(goodsPage,queryWrapper);
        }
        PageVO pageVO = new PageVO();
        pageVO.setTotal(resultPage.getTotal());
        pageVO.setData(resultPage.getRecords());
        return pageVO;
    }
}
