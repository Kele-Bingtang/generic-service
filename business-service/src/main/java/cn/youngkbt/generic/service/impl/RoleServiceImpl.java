package cn.youngkbt.generic.service.impl;

import cn.youngkbt.generic.common.model.GenericRole;
import cn.youngkbt.generic.mapper.RoleMapper;
import cn.youngkbt.generic.service.RoleService;
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
    public GenericRole queryRoleByUserAndProject(String username, String secretKey) {
        return roleMapper.queryRoleByUserAndSecret(username, secretKey);
    }

    public GenericRole queryRoleByCode(String code) {
        QueryWrapper<GenericRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        return roleMapper.selectOne(queryWrapper);
    }
}
