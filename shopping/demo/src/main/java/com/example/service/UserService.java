package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.form.entity.User;
import com.example.form.RuleForm;
import com.example.form.SearchForm;
import com.example.vo.PageVO;
import com.example.vo.ResultVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author admin
 * @since 2022-10-16
 */
public interface UserService extends IService<User> {
    public ResultVO login(RuleForm ruleform);
    public PageVO list(Integer page, Integer size);

    public PageVO search(SearchForm searchForm);
}
