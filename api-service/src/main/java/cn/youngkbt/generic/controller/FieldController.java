package cn.youngkbt.generic.controller;

import cn.youngkbt.generic.common.dto.field.FieldDeleteDTO;
import cn.youngkbt.generic.common.dto.field.FieldInsertDTO;
import cn.youngkbt.generic.common.dto.field.FieldUpdateDTO;
import cn.youngkbt.generic.common.http.HttpResult;
import cn.youngkbt.generic.common.http.Response;
import cn.youngkbt.generic.common.model.GenericField;
import cn.youngkbt.generic.service.FieldService;
import cn.youngkbt.generic.vo.FieldVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 23:45
 * @note 属性相关接口
 */
@RestController
@RequestMapping("/field")
@Validated
public class FieldController {

    @Resource
    private FieldService fieldService;

    @GetMapping("/queryFieldListPages/{serviceId}")
    public Response<List<FieldVO>> queryFieldListPages(@NotNull(message = "接口 id 不能为空") @PathVariable Integer serviceId,
                                                       @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                       @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericField> page = new Page<>(pageNo, pageSize);
        List<GenericField> genericFields = fieldService.queryFieldByPages(serviceId, page);
        return HttpResult.ok(this.getFieldVOList(genericFields));
    }

    @PostMapping("/insertField")
    public Response<Object> insertField(@Validated @RequestBody FieldInsertDTO fieldInsertDTO) {
        Map<String, Object> result = fieldService.insertField(fieldInsertDTO);
        return HttpResult.okOrFail(result.get("data"), (String) result.get("message"));
    }

    @PostMapping("/updateField")
    public Response<String> updateField(@Validated @RequestBody FieldUpdateDTO fieldUpdateDTO) {
        String result = fieldService.updateField(fieldUpdateDTO);
        return HttpResult.okMessage(result);
    }

    @PostMapping("/deleteFieldById")
    public Response<String> deleteFieldById(@Validated @RequestBody FieldDeleteDTO fieldDeleteDTO) {
        String result = fieldService.deleteField(fieldDeleteDTO);
        return HttpResult.okMessage(result);
    }

    public List<FieldVO> getFieldVOList(List<GenericField> fieldList) {
        List<FieldVO> fieldVOList = new ArrayList<>();
        fieldList.forEach(field -> {
            FieldVO vo = new FieldVO();
            BeanUtils.copyProperties(field, vo);
            fieldVOList.add(vo);
        });
        return fieldVOList;
    }
}
