package cn.youngkbt.generic.mapper;

import cn.youngkbt.generic.common.model.GenericRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Kele-Bingtang
 * @date 2022/12/20 20:50
 * @note
 */
@Mapper
public interface RoleMapper extends BaseMapper<GenericRole> {
    /**
     * 通过用户名和项目密钥查询所在项目的角色
     * @param username 用户名
     * @param secretKey 项目密钥
     * @return 角色
     */
    public GenericRole queryRoleByUserAndSecret(@Param("username") String username, @Param("secretKey") String secretKey);
    
}
