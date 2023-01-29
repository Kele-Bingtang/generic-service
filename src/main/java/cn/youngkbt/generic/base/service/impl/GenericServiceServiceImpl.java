package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.GenericApiMapper;
import cn.youngkbt.generic.base.mapper.GenericServiceMapper;
import cn.youngkbt.generic.base.model.GenericReport;
import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.service.GenericReportService;
import cn.youngkbt.generic.base.service.GenericServiceService;
import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.exception.GenericException;
import cn.youngkbt.generic.http.ResponseStatusEnum;
import cn.youngkbt.generic.utils.SearchUtils;
import cn.youngkbt.generic.utils.SecurityUtils;
import cn.youngkbt.generic.utils.StringUtils;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @note 1.0
 * @date 2022-12-03 22:45:22
 */
@Service
public class GenericServiceServiceImpl implements GenericServiceService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GenericServiceMapper genericServiceMapper;
    @Resource
    private GenericReportService genericReportService;
    @Resource
    private ServiceColService serviceColService;
    @Resource
    private GenericApiMapper genericApiMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public GenericService queryGenericServiceById(Integer serviceId) {
        return genericServiceMapper.selectById(serviceId);
    }

    @Override
    public List<GenericService> queryGenericServiceByConditions(List<ConditionVo> conditionVos) {
        QueryWrapper<GenericService> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericService.class);
        try {
            return genericServiceMapper.selectList(queryWrapper);
        } catch (Exception e) {
            throw new GenericException(ResponseStatusEnum.CONDITION_SQL_ERROR);
        }
    }

    @Override
    public GenericService queryOneGenericService(GenericService genericService) {
        QueryWrapper<GenericService> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(genericService);
        return genericServiceMapper.selectOne(queryWrapper);
    }

    @Override
    public List<GenericService> queryGenericServiceList(GenericService genericService) {
        String key = SecurityUtils.getUsername() + "_service_" + genericService.toString();
        // 从 Redis 拿缓存
        Object o = redisTemplate.opsForValue().get(key);
        if (o instanceof List) {
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
        if (o instanceof List) {
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
            throw new GenericException(ResponseStatusEnum.CONDITION_SQL_ERROR);
        }
    }

    @Override
    public IPage<GenericService> queryGenericServiceConditionsPages(IPage<GenericService> page, List<ConditionVo> conditionVos) {
        QueryWrapper<GenericService> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericService.class);
        try {
            return genericServiceMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            throw new GenericException(ResponseStatusEnum.CONDITION_SQL_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenericService insertGenericService(GenericService genericService) {
        String selectSql = genericService.getSelectSql();
        if (StringUtils.isNotBlank(selectSql)) {
            // 执行一次 sql，进行验证
            serviceColService.executeSql(selectSql);
        }
        genericService.setReportTitle(genericService.getServiceName());
        int i = genericServiceMapper.insert(genericService);
        GenericReport genericReport = new GenericReport();
        genericReport.setReportTitle(genericService.getServiceName());
        genericReport.setDescription(genericService.getServiceName());
        genericReport.setCreateUser(genericService.getCreateUser());
        genericReport.setModifyUser(genericService.getCreateUser());
        genericReport.setServiceId(genericService.getId());
        genericReportService.insertGenericReport(genericReport);
        serviceColService.queryColumnInfoAndInsert(genericService.getId(), selectSql);
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
        if (i > 0) {
            this.deleteCachedKeys();
        }
        return i;
    }

    @Override
    public List<String> genericServiceService(String databaseName) {
        // MySQL 
        String sql = "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = '" + databaseName + "'";
        return genericApiMapper.genericSelectTable(sql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteGenericServiceByIds(List<Integer> ids) {
        // 删除报表
        genericReportService.deleteGenericReportByIds(ids);
        // 删除 ServiceCol
        serviceColService.deleteServiceColByIds(ids);
        int i = genericServiceMapper.deleteBatchIds(ids);
        if (i > 0) {
            this.deleteCachedKeys();
        }
        return i;
    }

    public void deleteCachedKeys() {
        Set<String> keys = redisTemplate.keys(SecurityUtils.getUsername() + "_service*");
        if (null != keys) {
            Long delete = redisTemplate.delete(keys);
            if (null != delete) {
                LOGGER.info("删除了 {} 个 key", delete);
            }
        }
    }
}