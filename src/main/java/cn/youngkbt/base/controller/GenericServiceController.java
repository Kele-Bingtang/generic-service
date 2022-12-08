package cn.youngkbt.base.controller;

import java.util.List;

import cn.youngkbt.base.model.GenericService;
import cn.youngkbt.http.Response;
import cn.youngkbt.http.HttpResult;
import cn.youngkbt.base.service.impl.GenericServiceServiceImpl;
import cn.youngkbt.utils.SearchUtil;
import cn.youngkbt.valid.ValidList;
import cn.youngkbt.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Kele-Bingtang
 * @since 2022-12-03 22:45:22
 * @version 1.0
 */
@RestController
@RequestMapping("/genericService")
public class GenericServiceController {

	@Autowired
	private GenericServiceServiceImpl genericServiceService;

	@GetMapping("/queryGenericServiceByConditions")
	public Response queryGenericServiceByConditions(@RequestBody List<ConditionVo> conditionVos) {
		QueryWrapper<GenericService> queryWrapper = SearchUtil.parseWhereSql(conditionVos, GenericService.class);
		List<GenericService> service = genericServiceService.queryGenericServiceByConditions(queryWrapper);
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
		return HttpResult.ok(serviceListPages);
	}

	@GetMapping("/queryGenericServicePages")
	public Response queryGenericServicePages(@Validated @RequestBody(required = false) ValidList<ConditionVo> conditionVos, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
		IPage<GenericService> page = new Page<>(pageNo, pageSize);
		QueryWrapper<GenericService> queryWrapper = SearchUtil.parseWhereSql(conditionVos, GenericService.class);
		IPage<GenericService> categoryList = genericServiceService.queryGenericServiceConditionsPages(page, queryWrapper);
		return HttpResult.ok(categoryList);
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