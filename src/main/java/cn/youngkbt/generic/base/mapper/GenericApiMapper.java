package cn.youngkbt.generic.base.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 23:27
 * @note
 */
@Mapper
public interface GenericApiMapper {
    
    @Select("${sql}")
    public List<LinkedHashMap<String, Object>> commonSelect(@Param("sql") String sql);
}
