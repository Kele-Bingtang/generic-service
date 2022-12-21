package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.valid.ValidList;
import cn.youngkbt.generic.vo.ConditionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@RestController
@RequestMapping("/serviceCol")
public class ServiceColController {

	@Autowired
	private ServiceColService serviceColService;

	@GetMapping("/queryServiceColByConditions")
	public Response queryServiceColByConditions(@Validated @RequestBody ValidList<ConditionVo> conditionVos) {
		List<ServiceCol> colList = serviceColService.queryServiceColByConditions(conditionVos);
		return HttpResult.ok(colList);
	}

	@GetMapping("/queryServiceColList")
	public Response queryServiceColList(ServiceCol serviceCol) {
		List<ServiceCol> colList = serviceColService.queryServiceColList(serviceCol);
		return HttpResult.ok(colList);
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

}