package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.dto.report.ReportQueryDTO;
import cn.youngkbt.generic.base.dto.report.ReportUpdateDTO;
import cn.youngkbt.generic.base.service.ReportService;
import cn.youngkbt.generic.base.vo.ReportVO;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
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
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    @GetMapping("/queryReportList")
    public Response<List<ReportVO>> queryReportList(ReportQueryDTO reportQueryDTO) {
        List<ReportVO> reportVOList = reportService.queryReportList(reportQueryDTO);
        return HttpResult.ok(reportVOList);
    }

    @GetMapping("/queryOneReport")
    public Response<ReportVO> queryOneReport(@Validated ReportQueryDTO reportQueryDTO) {
        ReportVO reportVO = reportService.queryOneReport(reportQueryDTO);
        return HttpResult.ok(reportVO);
    }

    @PostMapping("/updateReport")
    public Response<String> updateReport(@Validated @RequestBody ReportUpdateDTO reportUpdateDTO) {
        String result = reportService.updateReport(reportUpdateDTO);
        return HttpResult.okMessage(result);
    }

}