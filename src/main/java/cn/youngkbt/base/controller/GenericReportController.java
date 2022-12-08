package cn.youngkbt.base.controller;

import java.util.List;

import cn.youngkbt.base.model.GenericProject;
import cn.youngkbt.base.model.GenericReport;
import cn.youngkbt.base.service.impl.GenericReportServiceImpl;
import cn.youngkbt.http.Response;
import cn.youngkbt.http.HttpResult;
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
@RequestMapping("/genericReport")
public class GenericReportController {

	@Autowired
	private GenericReportServiceImpl genericReportService;

	@GetMapping("/queryGenericReportByConditions")
	public Response queryGenericReportByConditions(@RequestBody List<ConditionVo> conditionVos) {
		QueryWrapper<GenericReport> queryWrapper = SearchUtil.parseWhereSql(conditionVos, GenericReport.class);
		List<GenericReport>  report = genericReportService.queryGenericReportByCondition(queryWrapper);
		return HttpResult.ok(report);
	}

	@GetMapping("/queryGenericReportList")
	public Response queryGenericReportList(GenericReport genericReport) {
		List<GenericReport> reportList = genericReportService.queryGenericReportList(genericReport);
		return HttpResult.ok(reportList);
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