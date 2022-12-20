package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.GenericRoleMapper;
import cn.youngkbt.generic.base.model.GenericRole;
import cn.youngkbt.generic.base.service.GenericRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Kele-Bingtang
 * @date 2022/12/20 20:56
 * @note
 */
@Service
public class GenericRoleServiceImpl implements GenericRoleService {

    @Resource
    private GenericRoleMapper genericRoleMapper;

    @Override
    public GenericRole queryGenericRoleByUserAndSecret(String username, String secretKey) {
        return genericRoleMapper.queryGenericRoleByUserAndSecret(username, secretKey);
    }

    public GenericRole queryGenericRoleByCode(String code) {
        QueryWrapper<GenericRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        return genericRoleMapper.selectOne(queryWrapper);
    }
}
