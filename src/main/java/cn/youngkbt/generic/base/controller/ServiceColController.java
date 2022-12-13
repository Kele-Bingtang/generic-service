package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.utils.SearchUtils;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @since 2022-12-03 22:45:22
 * @version 1.0
 */
@RestController
@RequestMapping("/serviceCol")
public class ServiceColController {

	@Autowired
	private ServiceColService serviceColService;

	@GetMapping("/queryServiceColByConditions")
	public Response queryServiceColByConditions(@RequestBody List<ConditionVo> conditionVos) {
		QueryWrapper<ServiceCol> queryWrapper = SearchUtils.parseWhereSql(conditionVos, ServiceCol.class);
		List<ServiceCol> colList = serviceColService.queryServiceColByConditions(queryWrapper);
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