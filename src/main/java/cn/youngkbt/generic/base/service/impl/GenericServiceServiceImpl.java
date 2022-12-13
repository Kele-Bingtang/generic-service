package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.GenericServiceMapper;
import cn.youngkbt.generic.base.model.GenericReport;
import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.service.GenericReportService;
import cn.youngkbt.generic.base.service.GenericServiceService;
import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.exception.ConditionSqlException;
import cn.youngkbt.generic.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Kele-Bingtang
 * @version 1.0
 * @since 2022-12-03 22:45:22
 */
@Service
public class GenericServiceServiceImpl implements GenericServiceService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GenericServiceMapper genericServiceMapper;
    @Autowired
    private GenericReportService genericReportService;
    @Autowired
    private ServiceColService serviceColService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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
        String key = SecurityUtils.getUsername() + "_service_" + genericService.toString();
        // 从 Redis 拿缓存
        Object o = redisTemplate.opsForValue().get(key);
        if(o instanceof List) {
            List<GenericService> serviceList = (List<GenericService>) o;
            LOGGER.info("从 Redis 拿到数据：{}", serviceList);
            return serviceList;
        }
        QueryWrapper<GenericService> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(genericService);
        List<GenericService> serviceList = genericServiceMapper.selectList(queryWrapper);
        // 存入 Redis
        redisTemplate.opsForValue().set(key, serviceList, 24, TimeUnit.HOURS);
        return serviceList;
    }

    @Override
    public IPage<GenericService> queryGenericServiceListPages(IPage<GenericService> page, GenericService genericService) {
        String key = SecurityUtils.getUsername() + "_service_" + page.getCurrent() + "_" + page.getSize() + "_" + genericService.toString();
        // 从 Redis 拿缓存
        Object o = redisTemplate.opsForValue().get(key);
        if(o instanceof List) {
            IPage<GenericService> servicePage = (IPage<GenericService>) o;
            LOGGER.info("从 Redis 拿到数据：{}", servicePage);
            return servicePage;
        }
        QueryWrapper<GenericService> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(genericService);
        try {
            IPage<GenericService> servicePage = genericServiceMapper.selectPage(page, queryWrapper);
            // 存入 Redis
            redisTemplate.opsForValue().set(key, servicePage, 24, TimeUnit.HOURS);
            return servicePage;
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
        } else {
            this.deleteCachedKeys();
        }
        return genericService;
    }

    @Override
    public GenericService updateGenericService(GenericService genericService) {
        int i = genericServiceMapper.updateById(genericService);
        if (i == 0) {
            return null;
        } else {
            this.deleteCachedKeys();
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
        } else {
            this.deleteCachedKeys();
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
        int i = genericServiceMapper.delete(queryWrapper);
        if(i > 0) {
            this.deleteCachedKeys();
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteGenericServiceByIds(List<Integer> ids) {
        // 删除报表
        genericReportService.deleteGenericReportByIds(ids);
        // 删除 ServiceCol
        serviceColService.deleteServiceColByIds(ids);
        int i = genericServiceMapper.deleteBatchIds(ids);
        if(i > 0) {
            this.deleteCachedKeys();
        }
        return i;
    }

    public void deleteCachedKeys() {
        Set<String> keys = redisTemplate.keys(SecurityUtils.getUsername() + "_service_*");
        if(null != keys) {
            Long delete = redisTemplate.delete(keys);
            if(null != delete) {
                LOGGER.info("删除了 {} 个 key", delete);
            }
        }
    }
}