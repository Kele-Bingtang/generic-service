package cn.youngkbt.generic.base.service;

import cn.youngkbt.generic.base.model.GenericRole;

/**
 * @author Kele-Bingtang
 * @date 2022/12/20 20:52
 * @note
 */
public interface GenericRoleService {
    public GenericRole queryGenericRoleByUserAndSecret(String username, String secretKey);
    
    public GenericRole queryGenericRoleByCode(String code);
}
