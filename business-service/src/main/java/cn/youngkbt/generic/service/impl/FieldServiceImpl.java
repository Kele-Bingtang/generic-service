package cn.youngkbt.generic.service.impl;

import cn.youngkbt.generic.common.dto.field.FieldDeleteDTO;
import cn.youngkbt.generic.common.dto.field.FieldInsertDTO;
import cn.youngkbt.generic.common.dto.field.FieldUpdateDTO;
import cn.youngkbt.generic.common.exception.GenericException;
import cn.youngkbt.generic.common.http.ResponseStatusEnum;
import cn.youngkbt.generic.common.model.GenericField;
import cn.youngkbt.generic.common.utils.ObjectUtils;
import cn.youngkbt.generic.mapper.FieldMapper;
import cn.youngkbt.generic.service.FieldService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 23:49
 * @note
 */
@Service
public class FieldServiceImpl implements FieldService {

    @Resource
    private FieldMapper fieldMapper;

    @Override
    public List<GenericField> queryFieldByPages(Integer serviceId, IPage<GenericField> page) {
        QueryWrapper<GenericField> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_id", serviceId);
        IPage<GenericField> genericFieldPage = fieldMapper.selectPage(page, queryWrapper);
        return genericFieldPage.getRecords();
    }

    @Override
    public List<GenericField> queryFieldByJsonCol(Integer serviceId) {
        QueryWrapper<GenericField> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("field", "parent_field", "json_col").eq("service_id", serviceId);
        List<GenericField> fieldList = fieldMapper.selectList(queryWrapper);
        return fieldList;
    }

    @Override
    public Map<String, Object> insertField(FieldInsertDTO fieldInsertDTO) {
        // 先判断是否存在属性
        QueryWrapper<GenericField> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("field", fieldInsertDTO.getField());
        GenericField genericField = fieldMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(genericField) && genericField.getId() > 0) {
            throw new GenericException(ResponseStatusEnum.FIELD_EXEIT);
        }
        GenericField field = new GenericField();
        BeanUtils.copyProperties(fieldInsertDTO, field);
        int result = fieldMapper.insert(field);
        if (result == 0) {
            throw new GenericException(ResponseStatusEnum.FAIL);
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("data", field);
        map.put("message", "新增成功");
        return map;
    }

    @Override
    public String updateField(FieldUpdateDTO fieldUpdateDTO) {
        GenericField field = new GenericField();
        BeanUtils.copyProperties(fieldUpdateDTO, field);
        QueryWrapper<GenericField> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", field.getId());
        int result = fieldMapper.update(field, queryWrapper);
        if (result == 0) {
            throw new GenericException(ResponseStatusEnum.FAIL);
        }
        return "更新成功";
    }

    @Override
    public String deleteField(FieldDeleteDTO fieldDeleteDTO) {
        int result = fieldMapper.deleteById(fieldDeleteDTO.getId());
        if (result == 0) {
            throw new GenericException(ResponseStatusEnum.FAIL);
        }
        return "删除成功";
    }
}
