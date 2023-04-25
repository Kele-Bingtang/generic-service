package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.dto.report.ReportQueryDTO;
import cn.youngkbt.generic.base.dto.report.ReportUpdateDTO;
import cn.youngkbt.generic.base.mapper.ReportMapper;
import cn.youngkbt.generic.base.model.GenericReport;
import cn.youngkbt.generic.base.service.ReportService;
import cn.youngkbt.generic.base.vo.ReportVO;
import cn.youngkbt.generic.exception.GenericException;
import cn.youngkbt.generic.http.ResponseStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@Service
public class ReportServiceImpl implements ReportService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ReportMapper reportMapper;

    @Override
    public List<ReportVO> queryReportList(ReportQueryDTO reportQueryDTO) {
        GenericReport report = new GenericReport();
        BeanUtils.copyProperties(reportQueryDTO, report);
        QueryWrapper<GenericReport> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(report);
        List<GenericReport> reportList = reportMapper.selectList(queryWrapper);
        return this.getReportVOList(reportList);
    }

    @Override
    public ReportVO queryOneReport(ReportQueryDTO reportQueryDTO) {
        GenericReport report = new GenericReport();
        BeanUtils.copyProperties(reportQueryDTO, report);
        QueryWrapper<GenericReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(report);
        GenericReport genericReport = reportMapper.selectOne(queryWrapper);
        ReportVO reportVO = new ReportVO();
        BeanUtils.copyProperties(genericReport, reportVO);
        return reportVO;
    }

    @Override
    public String insertReport(GenericReport genericReport) {
        int result = reportMapper.insert(genericReport);
        if (result == 0) {
            throw new GenericException(ResponseStatusEnum.FAIL);
        }
        return "插入成功";
    }

    @Override
    public String updateReport(ReportUpdateDTO reportUpdateDTO) {
        GenericReport report = new GenericReport();
        BeanUtils.copyProperties(reportUpdateDTO, report);
        int result = reportMapper.updateById(report);
        if (result == 0) {
            throw new GenericException(ResponseStatusEnum.FAIL);
        }
        return "修改成功";
    }

    @Override
    public int deleteReportByColumns(QueryWrapper<GenericReport> queryWrapper) {
        return reportMapper.delete(queryWrapper);
    }

    @Override
    public int deleteReportByIds(List<Integer> ids) {
        return reportMapper.deleteBatchIds(ids);
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