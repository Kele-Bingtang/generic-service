package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.service.GenericServiceService;
import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.utils.ObjectUtils;
import cn.youngkbt.generic.valid.ValidList;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
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
	private GenericServiceService genericServiceService;

	@GetMapping("/queryServiceColByConditions")
	public Response queryServiceColByConditions(@Validated @RequestBody ValidList<ConditionVo> conditionVos) {
		List<ServiceCol> colList = serviceColService.queryServiceColByConditions(conditionVos);
		return HttpResult.ok(colList);
	}

	@GetMapping("/queryServiceColList")
	public Response queryServiceColList(@Validated ServiceCol serviceCol) {
		List<ServiceCol> colList = serviceColService.queryServiceColList(serviceCol);
		return HttpResult.ok(colList);
	}

	@GetMapping("/queryServiceColListPages")
	public Response queryServiceColListPages(@Validated ServiceCol serviceCol, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
		IPage<ServiceCol> page = new Page<>(pageNo, pageSize);
		IPage<ServiceCol> colList = serviceColService.queryServiceColListPage(page, serviceCol);
		return HttpResult.ok(colList.getRecords());
	}

	@PostMapping("/insertServiceCol")
	public Response insertServiceCol(@RequestBody ServiceCol serviceCol) {
		ServiceCol sc = serviceColService.insertServiceCol(serviceCol);
		return HttpResult.ok(sc);
	}

	@PostMapping("/updateServiceCol")
	public Response updateServiceCol(@RequestBody ServiceCol serviceCol) {
		ServiceCol sc = serviceColService.updateServiceCol(serviceCol);
		return HttpResult.ok(sc);
	}

	@PostMapping("/deleteServiceColById")
	public Response deleteServiceColById(@RequestBody ServiceCol serviceCol) {
		ServiceCol sc = serviceColService.deleteServiceColById(serviceCol);
		return HttpResult.ok(sc);
	}
	
	@PostMapping(value = "/updateColumn", headers = {"content-type=application/x-www-form-urlencoded"})
	public Response updateColumn(@NotNull(message = "?????????????????????") Integer serviceId) {
		GenericService genericService = genericServiceService.queryGenericServiceById(serviceId);
		if(ObjectUtils.isEmpty(genericService) || ObjectUtils.isEmpty(genericService.getId())) {
			return HttpResult.fail("?????????????????????");
		}
		boolean isSuccess = serviceColService.queryColumnInfoAndInsert(serviceId, genericService.getSelectSql());
		if(isSuccess) {
			return HttpResult.ok("????????????");
		}else {
			return HttpResult.fail("????????????");
		}
	}

}