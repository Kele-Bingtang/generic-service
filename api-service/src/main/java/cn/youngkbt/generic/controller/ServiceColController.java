package cn.youngkbt.generic.controller;

import cn.youngkbt.generic.common.dto.serviceCol.*;
import cn.youngkbt.generic.common.http.HttpResult;
import cn.youngkbt.generic.common.http.Response;
import cn.youngkbt.generic.common.model.GenericService;
import cn.youngkbt.generic.common.model.ServiceCol;
import cn.youngkbt.generic.common.utils.ObjectUtils;
import cn.youngkbt.generic.service.ServiceColService;
import cn.youngkbt.generic.service.ServiceService;
import cn.youngkbt.generic.vo.ServiceColVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@RestController
@RequestMapping("/serviceCol")
@Validated
public class ServiceColController {

    @Resource
    private ServiceColService serviceColService;
    @Resource
    private ServiceService serviceService;

    @GetMapping("/queryServiceColList")
    public Response<List<ServiceColVO>> queryServiceColList(@Validated ServiceColQueryDTO serviceColQueryDTO) {
        List<ServiceCol> serviceColList = serviceColService.queryServiceColList(serviceColQueryDTO);
        return HttpResult.ok(this.getServiceColVOList(serviceColList));
    }

    @GetMapping("/queryServiceColListPages")
    public Response<List<ServiceColVO>> queryServiceColListPages(@Validated ServiceColQueryDTO serviceColQueryDTO,
                                                                 @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                                 @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<ServiceCol> page = new Page<>(pageNo, pageSize);
        List<ServiceCol> serviceColList = serviceColService.queryServiceColListPage(page, serviceColQueryDTO);
        return HttpResult.ok(this.getServiceColVOList(serviceColList));
    }

    @PostMapping("/insertServiceCol")
    public Response<String> insertServiceCol(@Validated @RequestBody ServiceColInsertDTO serviceColInsertDTO) {
        String result = serviceColService.insertServiceCol(serviceColInsertDTO);
        return HttpResult.ok(result);
    }

    @PostMapping("/updateServiceCol")
    public Response<String> updateServiceCol(@Validated @RequestBody ServiceColUpdateDTO serviceColUpdateDTO) {
        String result = serviceColService.updateServiceCol(serviceColUpdateDTO);
        return HttpResult.ok(result);
    }

    /**
     * 批量更新字段
     * @param batchUpdateDTO 前端传来的参数
     * @return 成功与否
     */
    @PostMapping("/updateBatchServiceCol")
    public Response<String> updateBatchServiceCol(@Validated @RequestBody ServiceColBatchUpdateDTO batchUpdateDTO) {
        if (batchUpdateDTO.getJsonColList().isEmpty()) {
            return HttpResult.error("批量修改的字段不能为空");
        }
        if (ObjectUtils.isEmpty(batchUpdateDTO.getAllowInsert()) && ObjectUtils.isEmpty(batchUpdateDTO.getAllowUpdate()) &&
                ObjectUtils.isEmpty(batchUpdateDTO.getAllowFilter()) && ObjectUtils.isEmpty(batchUpdateDTO.getAllowRequest())
        ) {
            return HttpResult.error("批量修改的字段不能为空");
        }

        String result = serviceColService.updateBatchServiceCol(batchUpdateDTO);
        return HttpResult.ok(result);
    }

    @PostMapping("/deleteServiceColById")
    public Response<String> deleteServiceColById(@Validated @RequestBody ServiceColDeleteDTO serviceColDeleteDTO) {
        String result = serviceColService.deleteServiceColById(serviceColDeleteDTO);
        return HttpResult.ok(result);
    }

    /**
     * 重新更新新增的字段
     * @param serviceId 接口 id
     * @return 成功与否
     */
    @PostMapping(value = "/updateColumn/{serviceId}")
    public Response<String> updateColumn(@NotNull(message = "请传入接口信息") @PathVariable Integer serviceId) {
        GenericService genericService = serviceService.queryServiceById(serviceId);
        if (ObjectUtils.isEmpty(genericService) || ObjectUtils.isEmpty(genericService.getId())) {
            return HttpResult.fail("接口信息不存在");
        }
        Integer length = serviceColService.queryColumnInfoAndUpdate(serviceId, genericService.getSelectSql());
        if (length > 0) {
            return HttpResult.okMessage("更新成功，更新了 " + length + " 条");
        } else if (length == 0) {
            return HttpResult.okMessage("不需要更新，没有新增的字段");
        } else {
            return HttpResult.okMessage("更新失败，sql 为空");
        }
    }

    /**
     * 删除不存在的字段
     * @param serviceId 接口 id
     * @return 成功与否
     */
    @PostMapping(value = "/deleteInvalidColumn/{serviceId}")
    public Response<String> deleteInvalidColumn(@NotNull(message = "请传入接口信息") @PathVariable Integer serviceId) {
        GenericService genericService = serviceService.queryServiceById(serviceId);
        if (ObjectUtils.isEmpty(genericService) || ObjectUtils.isEmpty(genericService.getId())) {
            return HttpResult.fail("接口信息不存在");
        }
        Integer length = serviceColService.queryColumnInfoAndDelete(serviceId, genericService.getSelectSql());
        if (length > 0) {
            return HttpResult.okMessage("删除成功，删除了 " + length + " 条");
        } else if (length == 0) {
            return HttpResult.okMessage("不需要删除，没有失效的字段");
        } else {
            return HttpResult.okMessage("删除失败，sql 为空");
        }
    }

    public List<ServiceColVO> getServiceColVOList(List<ServiceCol> serviceColList) {
        List<ServiceColVO> serviceColVOList = new ArrayList<>();
        serviceColList.forEach(serviceCol -> {
            ServiceColVO vo = new ServiceColVO();
            BeanUtils.copyProperties(serviceCol, vo);
            serviceColVOList.add(vo);
        });
        return serviceColVOList;
    }
}