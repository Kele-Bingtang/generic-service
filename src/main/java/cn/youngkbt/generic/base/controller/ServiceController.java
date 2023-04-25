package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.dto.service.ServiceDeleteDTO;
import cn.youngkbt.generic.base.dto.service.ServiceInsertDTO;
import cn.youngkbt.generic.base.dto.service.ServiceQueryDTO;
import cn.youngkbt.generic.base.dto.service.ServiceUpdateDTO;
import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.service.ServiceService;
import cn.youngkbt.generic.base.vo.ServiceVO;
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
 * @date 2022-12-03 22:45:22
 * @note
 */
@RestController
@RequestMapping("/service")
public class ServiceController {

    @Resource
    private ServiceService serviceService;

    @GetMapping("/queryOneService")
    public Response<ServiceVO> queryOneService(@Validated ServiceQueryDTO serviceQueryDTO) {
        ServiceVO serviceVO = serviceService.queryOneGenericService(serviceQueryDTO);
        return HttpResult.ok(serviceVO);
    }

    @GetMapping("/queryServiceList")
    public Response<List<ServiceVO>> queryServiceList(ServiceQueryDTO serviceQueryDTO) {
        List<ServiceVO> serviceVOList = serviceService.queryServiceList(serviceQueryDTO);
        return HttpResult.ok(serviceVOList);
    }

    @GetMapping("/queryServiceListPages")
    public Response<List<ServiceVO>> queryServiceListPages(ServiceQueryDTO serviceQueryDTO,
                                                           @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                           @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericService> page = new Page<>(pageNo, pageSize);
        List<ServiceVO> serviceVOList = serviceService.queryServiceListPages(page, serviceQueryDTO);
        return HttpResult.ok(serviceVOList);
    }

    @PostMapping("/insertService")
    public Response<String> insertService(@Validated @RequestBody ServiceInsertDTO serviceInsertDTO) {
        String result = serviceService.insertService(serviceInsertDTO);
        return HttpResult.okMessage(result);
    }

    @PostMapping("/updateService")
    public Response<String> updateService(@Validated @RequestBody ServiceUpdateDTO serviceUpdateDTO) {
        String result = serviceService.updateService(serviceUpdateDTO);
        return HttpResult.okMessage(result);
    }

    @PostMapping("/deleteGenericServiceById")
    public Response<String> deleteServiceById(@Validated @RequestBody ServiceDeleteDTO serviceDeleteDTO) {
        String result = serviceService.deleteServiceById(serviceDeleteDTO);
        return HttpResult.okMessage(result);
    }

    @GetMapping("/queryTableViewNameByDatabaseName/{databaseName}")
    public Response<Map<String, List<String>>> queryTableViewNameByDatabaseName(@PathVariable("databaseName") String databaseName) {
        Map<String, List<String>> tableNameList = serviceService.queryTableViewNameList(databaseName);
        return HttpResult.okOrFail(tableNameList, "查询成功");
    }

    @PostMapping("/verifySql")
    public Response<Integer> verifySql(@NotNull(message = "sql 不允许为空") @RequestParam String sql) {
        Integer length = serviceService.verifySql(sql);
        return HttpResult.ok(length);
    }

}