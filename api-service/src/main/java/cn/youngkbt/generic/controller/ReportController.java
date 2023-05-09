package cn.youngkbt.generic.controller;

import cn.youngkbt.generic.common.dto.report.ReportQueryDTO;
import cn.youngkbt.generic.common.dto.report.ReportUpdateDTO;
import cn.youngkbt.generic.common.http.HttpResult;
import cn.youngkbt.generic.common.http.Response;
import cn.youngkbt.generic.common.model.GenericReport;
import cn.youngkbt.generic.service.ReportService;
import cn.youngkbt.generic.vo.ReportVO;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 报表相关接口
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    @GetMapping("/queryReportList")
    public Response<List<ReportVO>> queryReportList(ReportQueryDTO reportQueryDTO) {
        List<GenericReport> reportList = reportService.queryReportList(reportQueryDTO);
        return HttpResult.ok(this.getReportVOList(reportList));
    }

    @GetMapping("/queryOneReport")
    public Response<ReportVO> queryOneReport(@Validated ReportQueryDTO reportQueryDTO) {
        GenericReport report = reportService.queryOneReport(reportQueryDTO);
        ReportVO reportVO = new ReportVO();
        BeanUtils.copyProperties(report, reportVO);
        return HttpResult.ok(reportVO);
    }

    @PostMapping("/updateReport")
    public Response<String> updateReport(@Validated @RequestBody ReportUpdateDTO reportUpdateDTO) {
        String result = reportService.updateReport(reportUpdateDTO);
        return HttpResult.okMessage(result);
    }

    public List<ReportVO> getReportVOList(List<GenericReport> reportList) {
        List<ReportVO> reportVOList = new ArrayList<>();
        reportList.forEach(report -> {
            ReportVO vo = new ReportVO();
            BeanUtils.copyProperties(report, vo);
            reportVOList.add(vo);
        });
        return reportVOList;
    }

}