package cn.youngkbt.generic.mapper;

import cn.youngkbt.generic.common.model.GenericCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@Mapper
public interface CategoryMapper extends BaseMapper<GenericCategory> {

}