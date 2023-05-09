package cn.youngkbt.generic.service;

import cn.youngkbt.generic.common.dto.field.FieldDeleteDTO;
import cn.youngkbt.generic.common.dto.field.FieldInsertDTO;
import cn.youngkbt.generic.common.dto.field.FieldUpdateDTO;
import cn.youngkbt.generic.common.model.GenericField;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 23:35
 * @note
 */
public interface FieldService {

    /**
     * 分页查询属性
     * @return 所有数据的实体对象集合
     */
    public List<GenericField> queryFieldByPages(Integer serviceId, IPage<GenericField> page);
    /**
     * 通过 JsonCol 查询属性
     * @return 所有数据的实体对象集合
     */
    public List<GenericField> queryFieldByJsonCol(Integer serviceId);

    public Map<String, Object> insertField(FieldInsertDTO fieldInsertDTO);

    public String updateField(FieldUpdateDTO fieldUpdateDTO);

    public String deleteField(FieldDeleteDTO fieldDeleteDTO);
}
