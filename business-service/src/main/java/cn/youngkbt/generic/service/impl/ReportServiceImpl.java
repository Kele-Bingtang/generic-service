package cn.youngkbt.generic.service.impl;

import cn.youngkbt.generic.common.dto.report.ReportQueryDTO;
import cn.youngkbt.generic.common.dto.report.ReportUpdateDTO;
import cn.youngkbt.generic.common.exception.GenericException;
import cn.youngkbt.generic.common.http.ResponseStatusEnum;
import cn.youngkbt.generic.common.model.GenericReport;
import cn.youngkbt.generic.mapper.ReportMapper;
import cn.youngkbt.generic.service.ReportService;
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
    public List<GenericReport> queryReportList(ReportQueryDTO reportQueryDTO) {
        GenericReport report = new GenericReport();
        BeanUtils.copyProperties(reportQueryDTO, report);
        QueryWrapper<GenericReport> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(report);
        List<GenericReport> reportList = reportMapper.selectList(queryWrapper);
        return reportList;
    }

    @Override
    public GenericReport queryOneReport(ReportQueryDTO reportQueryDTO) {
        GenericReport report = new GenericReport();
        BeanUtils.copyProperties(reportQueryDTO, report);
        QueryWrapper<GenericReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(report);
        return reportMapper.selectOne(queryWrapper);
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

}