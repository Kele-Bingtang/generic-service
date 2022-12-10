package cn.youngkbt.generic.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.youngkbt.generic.base.model.GenericReport;
import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.service.GenericReportService;
import cn.youngkbt.generic.base.service.GenericServiceService;
import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.mapper.GenericServiceMapper;
import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.exception.ConditionSqlException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kele-Bingtang
 * @version 1.0
 * @since 2022-12-03 22:45:22
 */
@Service
public class GenericServiceServiceImpl implements GenericServiceService {

    @Autowired
    private GenericServiceMapper genericServiceMapper;
    @Autowired
    private GenericReportService genericReportService;
    @Autowired
    private ServiceColService serviceColService;

    @Override
    public List<GenericService> queryGenericServiceByConditions(QueryWrapper<GenericService> queryWrapper) {
        try {
            return genericServiceMapper.selectList(queryWrapper);
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    public List<GenericService> queryGenericServiceList(GenericService genericService) {
        QueryWrapper<GenericService> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(genericService);
        return genericServiceMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<GenericService> queryGenericServiceListPages(IPage<GenericService> page, GenericService genericService) {
        QueryWrapper<GenericService> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(genericService);
        try {
            return genericServiceMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    public IPage<GenericService> queryGenericServiceConditionsPages(IPage<GenericService> page, QueryWrapper<GenericService> queryWrapper) {
        try {
            return genericServiceMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    public GenericService insertGenericService(GenericService genericService) {
        int i = genericServiceMapper.insert(genericService);
        if (i == 0) {
            return null;
        }
        return genericService;
    }

    @Override
    public GenericService updateGenericService(GenericService genericService) {
        int i = genericServiceMapper.updateById(genericService);
        if (i == 0) {
            return null;
        }
        return genericService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenericService deleteGenericServiceById(GenericService genericService) {
        QueryWrapper<GenericReport> reportQueryWrapper = new QueryWrapper<>();
        QueryWrapper<ServiceCol> serviceColQueryWrapper = new QueryWrapper<>();
        reportQueryWrapper.eq("service_id", genericService.getId());
        serviceColQueryWrapper.eq("service_id", genericService.getId());
        // 根据接口的主键删除报表
        genericReportService.deleteGenericReportByColumns(reportQueryWrapper);
        // 删除 ServiceCol
        serviceColService.deleteServiceColByColumns(serviceColQueryWrapper);
        int i = genericServiceMapper.deleteById(genericService);
        if (i == 0) {
            return null;
        }
        return genericService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteGenericServiceByColumns(QueryWrapper<GenericService> queryWrapper) {
        queryWrapper.select("id");
        // 获取即将删除报销的主键
        List<GenericService> serviceList = genericServiceMapper.selectList(queryWrapper);
        if (!serviceList.isEmpty()) {
            List<Integer> ids = new ArrayList<>();
            serviceList.forEach(service -> ids.add(service.getId()));
            if (!ids.isEmpty()) {
                // 删除报表
                genericReportService.deleteGenericReportByIds(ids);
                // 删除 ServiceCol
                serviceColService.deleteServiceColByIds(ids);
            }
        }
        return genericServiceMapper.delete(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteGenericServiceByIds(List<Integer> ids) {
        // 删除报表
        genericReportService.deleteGenericReportByIds(ids);
        // 删除 ServiceCol
        serviceColService.deleteServiceColByIds(ids);
        return genericServiceMapper.deleteBatchIds(ids);
    }

}