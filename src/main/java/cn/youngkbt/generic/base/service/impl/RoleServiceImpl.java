package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.RoleMapper;
import cn.youngkbt.generic.base.model.GenericRole;
import cn.youngkbt.generic.base.service.RoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Kele-Bingtang
 * @date 2022/12/20 20:56
 * @note
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public GenericRole queryGenericRoleByUserAndProject(String username, String secretKey) {
        return roleMapper.queryGenericRoleByUserAndSecret(username, secretKey);
    }

    public GenericRole queryGenericRoleByCode(String code) {
        QueryWrapper<GenericRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        return roleMapper.selectOne(queryWrapper);
    }
}
