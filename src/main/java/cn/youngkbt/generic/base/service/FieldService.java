package cn.youngkbt.generic.base.service;

import cn.youngkbt.generic.base.dto.field.FieldDeleteDTO;
import cn.youngkbt.generic.base.dto.field.FieldInsertDTO;
import cn.youngkbt.generic.base.dto.field.FieldUpdateDTO;
import cn.youngkbt.generic.base.model.GenericField;
import cn.youngkbt.generic.base.vo.FieldVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 23:35
 * @note
 */
public interface FieldService {

    public List<FieldVO> queryFieldByPages(Integer serviceId, IPage<GenericField> page);
    
    public List<GenericField> queryFieldByJsonCol(Integer serviceId);

    public Map<String, Object> insertField(FieldInsertDTO fieldInsertDTO);

    public String updateField(FieldUpdateDTO fieldUpdateDTO);

    public String deleteField(FieldDeleteDTO fieldDeleteDTO);
}
