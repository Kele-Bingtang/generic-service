package cn.youngkbt.generic.base.mapper;

import cn.youngkbt.generic.base.model.GenericField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 23:33
 * @note
 */
@Mapper
public interface FieldMapper extends BaseMapper<GenericField> {
}
