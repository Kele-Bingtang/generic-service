package cn.youngkbt.generic.controller;

import cn.youngkbt.generic.common.dto.service.ServiceDeleteDTO;
import cn.youngkbt.generic.common.dto.service.ServiceInsertDTO;
import cn.youngkbt.generic.common.dto.service.ServiceQueryDTO;
import cn.youngkbt.generic.common.dto.service.ServiceUpdateDTO;
import cn.youngkbt.generic.common.dto.service.ServiceResultDTO;
import cn.youngkbt.generic.common.http.HttpResult;
import cn.youngkbt.generic.common.http.Response;
import cn.youngkbt.generic.common.model.GenericService;
import cn.youngkbt.generic.service.ServiceService;
import cn.youngkbt.generic.vo.ServiceVO;
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
 * @date 2022-12-03 22:45:22
 * @note 服务相关接口
 */
@RestController
@RequestMapping("/service")
public class ServiceController {

    @Resource
    private ServiceService serviceService;

    @GetMapping("/queryOneService")
    public Response<ServiceVO> queryOneService(@Validated ServiceQueryDTO serviceQueryDTO) {
        GenericService service = serviceService.queryOneGenericService(serviceQueryDTO);
        ServiceVO serviceVO = new ServiceVO();
        BeanUtils.copyProperties(service, serviceVO);
        return HttpResult.ok(serviceVO);
    }

    @GetMapping("/queryServiceList")
    public Response<List<ServiceVO>> queryServiceList(@Validated  ServiceQueryDTO serviceQueryDTO) {
        List<GenericService> serviceList = serviceService.queryServiceList(serviceQueryDTO);
        return HttpResult.ok(this.getServiceVOList(serviceList));
    }

    @GetMapping("/queryServiceListPages")
    public Response<List<ServiceVO>> queryServiceListPages(@Validated ServiceQueryDTO serviceQueryDTO,
                                                           @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                           @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericService> page = new Page<>(pageNo, pageSize);
        List<GenericService> serviceList = serviceService.queryServiceListPages(page, serviceQueryDTO);
        return HttpResult.ok(this.getServiceVOList(serviceList));
    }

    /**
     * 查询项目包含的接口和该接口包含的字段
     * @param projectId 项目 id
     * @return  项目包含的接口和该接口包含的字段
     */
    @GetMapping("/queryServiceAndServiceColList/{projectId}")
    public Response<List<ServiceResultDTO>> queryServiceAndServiceColList(@PathVariable Integer projectId) {
        List<ServiceResultDTO> serviceResult = serviceService.queryServiceAndServiceColList(projectId);
        return HttpResult.ok(serviceResult);
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

    @PostMapping("/deleteServiceById")
    public Response<String> deleteServiceById(@Validated @RequestBody ServiceDeleteDTO serviceDeleteDTO) {
        String result = serviceService.deleteServiceById(serviceDeleteDTO);
        return HttpResult.okMessage(result);
    }

    /**
     * 查询数据库所有的表和视图
     * @param databaseName 数据库
     * @return 数据库对应的所有的表和视图
     */
    @GetMapping("/queryTableViewNameByDatabaseName/{databaseName}")
    public Response<Map<String, List<String>>> queryTableViewNameByDatabaseName(@PathVariable("databaseName") String databaseName) {
        Map<String, List<String>> tableNameList = serviceService.queryTableViewNameList(databaseName);
        return HttpResult.okOrFail(tableNameList, "查询成功");
    }

    /**
     * 执行一条查询 SQL
     * @param sql 查询 sql 语句
     * @return sql 返回的个数
     */
    @PostMapping("/verifySql")
    public Response<Integer> verifySql(@NotNull(message = "sql 不允许为空") @RequestParam String sql) {
        Integer length = serviceService.verifySql(sql);
        return HttpResult.ok(length);
    }

    public List<ServiceVO> getServiceVOList(List<GenericService> serviceList) {
        List<ServiceVO> serviceVOList = new ArrayList<>();
        serviceList.forEach(service -> {
            ServiceVO vo = new ServiceVO();
            BeanUtils.copyProperties(service, vo);
            serviceVOList.add(vo);
        });
        return serviceVOList;
    }

}