package cn.youngkbt.generic.base.mapper;

import cn.youngkbt.generic.base.model.GenericService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:23
 * @note 1.0
 */
@Mapper
public interface GenericServiceMapper extends BaseMapper<GenericService> {
    
}