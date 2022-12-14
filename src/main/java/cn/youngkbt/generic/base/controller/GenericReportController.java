package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.model.GenericReport;
import cn.youngkbt.generic.base.service.GenericReportService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.valid.ValidList;
import cn.youngkbt.generic.vo.ConditionVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@RestController
@RequestMapping("/genericReport")
public class GenericReportController {

	@Resource
	private GenericReportService genericReportService;

	@GetMapping("/queryGenericReportByConditions")
	public Response queryGenericReportByConditions(@Validated @RequestBody ValidList<ConditionVo> conditionVos) {
		List<GenericReport>  report = genericReportService.queryGenericReportByCondition(conditionVos);
		return HttpResult.ok(report);
	}

	@GetMapping("/queryGenericReportList")
	public Response queryGenericReportList(GenericReport genericReport) {
		List<GenericReport> reportList = genericReportService.queryGenericReportList(genericReport);
		return HttpResult.ok(reportList);
	}
	@GetMapping("/queryOneGenericReport")
	public Response queryOneGenericReport(@Validated GenericReport genericReport) {
		GenericReport report = genericReportService.queryOneGenericReport(genericReport);
		return HttpResult.ok(report);
	}

	// @PostMapping("/insertGenericReport")
	// public Response insertGenericReport(@RequestBody GenericReport genericReport) {
	// 	GenericReport report = genericReportService.insertGenericReport(genericReport);
	// 	return HttpResult.okOrFail(report);
	// }

	@PostMapping("/updateGenericReport")
	public Response updateGenericReport(@Validated(GenericReport.ReportUpdate.class) @RequestBody GenericReport genericReport) {
		GenericReport report = genericReportService.updateGenericReport(genericReport);
		return HttpResult.okOrFail(report);
	}

	// @PostMapping("/deleteGenericReportById")
	// public Response deleteGenericReportById(@RequestBody GenericReport genericReport) {
	// 	GenericReport report = genericReportService.deleteGenericReportById(genericReport);
	// 	return HttpResult.okOrFail(report);
	// }

}