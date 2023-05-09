package cn.youngkbt.generic.service;


import cn.youngkbt.generic.common.model.GenericRole;

/**
 * @author Kele-Bingtang
 * @date 2022/12/20 20:52
 * @note
 */
public interface RoleService {
    public GenericRole queryRoleByUserAndProject(String username, String secretKey);
    
    public GenericRole queryRoleByCode(String code);
}
