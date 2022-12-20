package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.form.entity.Goods;
import com.example.form.SearchForm;
import com.example.vo.PageVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2022-10-16
 */
public interface GoodsService extends IService<Goods> {
    public PageVO list(Integer page, Integer size);

    public PageVO search(SearchForm searchForm);
}
