package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.service.GenericServiceService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.valid.ValidList;
import cn.youngkbt.generic.vo.ConditionVo;
import cn.youngkbt.generic.vo.GenericServiceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 
 */
@RestController
@RequestMapping("/genericService")
public class GenericServiceController {

    @Autowired
    private GenericServiceService genericServiceService;

    @GetMapping("/queryGenericServiceByConditions")
    public Response queryGenericServiceByConditions(@Validated @RequestBody ValidList<ConditionVo> conditionVos) {
        List<GenericService> service = genericServiceService.queryGenericServiceByConditions(conditionVos);
        return HttpResult.ok(service);
    }

    @GetMapping("/queryGenericServiceList")
    public Response queryGenericServiceList(GenericService genericService) {
        List<GenericService> serviceList = genericServiceService.queryGenericServiceList(genericService);
        return HttpResult.ok(serviceList);
    }

    @GetMapping("/queryGenericServiceListPages")
    public Response queryGenericServiceListPages(GenericService genericService, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericService> page = new Page<>(pageNo, pageSize);
        IPage<GenericService> serviceListPages = genericServiceService.queryGenericServiceListPages(page, genericService);
        List<GenericServiceVo> serviceVoList = new ArrayList<>();
        List<GenericService> records = serviceListPages.getRecords();
        records.forEach(item -> {
            GenericServiceVo serviceVo = new GenericServiceVo();
            BeanUtils.copyProperties(item, serviceVo);
            if (item.getStatus() == 0) {
                serviceVo.setStatus("启用");
            } else if (item.getStatus() == 1) {
                serviceVo.setStatus("禁用");
            }
            serviceVoList.add(serviceVo);
        });
        return HttpResult.ok(serviceVoList);
    }

    @GetMapping("/queryGenericServiceConditionsPages")
    public Response queryGenericServiceConditionsPages(@Validated @RequestBody(required = false) ValidList<ConditionVo> conditionVos, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericService> page = new Page<>(pageNo, pageSize);
        IPage<GenericService> categoryList = genericServiceService.queryGenericServiceConditionsPages(page, conditionVos);
        return HttpResult.ok(categoryList.getRecords());
    }

    @PostMapping("/insertGenericService")
    public Response insertGenericService(@Validated(GenericService.ServiceInsert.class) @RequestBody GenericService genericService) {
        GenericService service = genericServiceService.insertGenericService(genericService);
        return HttpResult.okOrFail(service);
    }

    @PostMapping("/updateGenericService")
    public Response updateGenericService(@Validated(GenericService.ServiceUpdate.class) @RequestBody GenericService genericService) {
        GenericService service = genericServiceService.updateGenericService(genericService);
        return HttpResult.okOrFail(service);
    }

    @PostMapping("/deleteGenericServiceById")
    public Response deleteGenericServiceById(@Validated(GenericService.ServiceDelete.class) @RequestBody GenericService genericService) {
        GenericService service = genericServiceService.deleteGenericServiceById(genericService);
        return HttpResult.okOrFail(service);
    }

}