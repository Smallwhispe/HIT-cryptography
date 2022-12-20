package com.example.service;

import com.example.form.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.form.RuleForm;
import com.example.form.SearchForm;
import com.example.vo.PageVO;
import com.example.vo.ResultVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2022-10-16
 */
public interface ShopService extends IService<Shop> {
    public ResultVO login(RuleForm ruleform);
    public PageVO list(Integer page, Integer size);

    public PageVO search(SearchForm searchForm);
}
