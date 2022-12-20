package cn.youngkbt.generic.base.mapper;

import cn.youngkbt.generic.base.model.GenericRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Kele-Bingtang
 * @date 2022/12/20 20:50
 * @note
 */
@Mapper
public interface GenericRoleMapper extends BaseMapper<GenericRole> {
    
    public GenericRole queryGenericRoleByUserAndSecret(@Param("username") String username,@Param("secretKey") String secretKey);
    
}
