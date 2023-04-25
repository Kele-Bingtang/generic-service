package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.dto.field.FieldDeleteDTO;
import cn.youngkbt.generic.base.dto.field.FieldInsertDTO;
import cn.youngkbt.generic.base.dto.field.FieldUpdateDTO;
import cn.youngkbt.generic.base.model.GenericField;
import cn.youngkbt.generic.base.service.FieldService;
import cn.youngkbt.generic.base.vo.FieldVO;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 23:45
 * @note
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
        List<FieldVO> genericFields = fieldService.queryFieldByPages(serviceId, page);
        return HttpResult.ok(genericFields);
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
}
