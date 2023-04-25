package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.dto.service.ServiceDeleteDTO;
import cn.youngkbt.generic.base.dto.service.ServiceInsertDTO;
import cn.youngkbt.generic.base.dto.service.ServiceQueryDTO;
import cn.youngkbt.generic.base.dto.service.ServiceUpdateDTO;
import cn.youngkbt.generic.base.mapper.ApiMapper;
import cn.youngkbt.generic.base.mapper.ServiceMapper;
import cn.youngkbt.generic.base.model.GenericReport;
import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.service.ReportService;
import cn.youngkbt.generic.base.service.ServiceService;
import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.base.vo.ServiceVO;
import cn.youngkbt.generic.exception.GenericException;
import cn.youngkbt.generic.http.ResponseStatusEnum;
import cn.youngkbt.generic.utils.ObjectUtils;
import cn.youngkbt.generic.utils.SecurityUtils;
import cn.youngkbt.generic.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Kele-Bingtang
 * @note 1.0
 * @date 2022-12-03 22:45:22
 */
@Service
public class ServiceServiceImpl implements ServiceService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ServiceMapper serviceMapper;
    @Resource
    private ReportService reportService;
    @Resource
    private ServiceColService serviceColService;
    @Resource
    private ApiMapper apiMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public GenericService queryGenericServiceById(Integer serviceId) {
        return serviceMapper.selectById(serviceId);
    }

    @Override
    public ServiceVO queryOneGenericService(ServiceQueryDTO serviceQueryDTO) {
        GenericService service = new GenericService();
        BeanUtils.copyProperties(serviceQueryDTO, service);
        QueryWrapper<GenericService> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(service);
        GenericService genericService = serviceMapper.selectOne(queryWrapper);
        ServiceVO serviceVO = new ServiceVO();
        BeanUtils.copyProperties(genericService, serviceVO);
        return serviceVO;
    }

    @Override
    public List<ServiceVO> queryServiceList(ServiceQueryDTO serviceQueryDTO) {
        GenericService service = new GenericService();
        BeanUtils.copyProperties(serviceQueryDTO, service);
        String key = SecurityUtils.getUsername() + "_service_" + service.toString();
        // 从 Redis 拿缓存
        // Object o = redisTemplate.opsForValue().get(key);
        // if (o instanceof List) {
        //     List<ServiceVO> serviceList = (List<ServiceVO>) o;
        //     LOGGER.info("从 Redis 拿到数据：{}", serviceList);
        //     return serviceList;
        // }
        QueryWrapper<GenericService> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(service);
        List<GenericService> serviceList = serviceMapper.selectList(queryWrapper);
        List<ServiceVO> serviceVOList = this.getServiceVOList(serviceList);
        // 存入 Redis
        // redisTemplate.opsForValue().set(key, serviceVOList, 24, TimeUnit.HOURS);
        return serviceVOList;
    }

    @Override
    public List<ServiceVO> queryServiceListPages(IPage<GenericService> page, ServiceQueryDTO serviceQueryDTO) {
        GenericService service = new GenericService();
        BeanUtils.copyProperties(serviceQueryDTO, service);
        String key = SecurityUtils.getUsername() + "_service_" + page.getCurrent() + "_" + page.getSize() + "_" + service.toString();
        // 从 Redis 拿缓存
        // Object o = redisTemplate.opsForValue().get(key);
        // if (o instanceof List) {
        //     List<ServiceVO> servicePage = (List<ServiceVO>) o;
        //     LOGGER.info("从 Redis 拿到数据：{}", servicePage);
        //     return servicePage;
        // }
        QueryWrapper<GenericService> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(service);
        try {
            IPage<GenericService> servicePage = serviceMapper.selectPage(page, queryWrapper);
            List<ServiceVO> serviceVOList = this.getServiceVOList(servicePage.getRecords());
            // 存入 Redis
            // redisTemplate.opsForValue().set(key, serviceVOList, 24, TimeUnit.HOURS);
            return serviceVOList;
        } catch (Exception e) {
            throw new GenericException(ResponseStatusEnum.CONDITION_SQL_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertService(ServiceInsertDTO serviceInsertDTO) {
        // 先判断是否存在接口
        QueryWrapper<GenericService> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_name", serviceInsertDTO.getServiceName());
        GenericService genericService = serviceMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(genericService) && genericService.getId() > 0) {
            throw new GenericException(ResponseStatusEnum.SERVICE_COL_EXEIT);
        }
        GenericService service = new GenericService();
        BeanUtils.copyProperties(serviceInsertDTO, service);
        if (StringUtils.isBlank(service.getReportTitle())) {
            service.setReportTitle(service.getServiceName());
        }
        int result = serviceMapper.insert(service);
        if (result != 0) {
            String selectSql = service.getSelectSql();
            GenericReport genericReport = new GenericReport();
            genericReport.setReportTitle(service.getServiceName());
            genericReport.setDescription(service.getServiceName());
            genericReport.setCreateUser(service.getCreateUser());
            genericReport.setModifyUser(service.getCreateUser());
            genericReport.setServiceId(service.getId());
            reportService.insertReport(genericReport);
            serviceColService.queryColumnInfoAndInsert(service.getId(), selectSql);
            service.setReportTitle(service.getServiceName());
        } else {
            throw new GenericException(ResponseStatusEnum.FAIL);
        }
        return this.response(result, "插入成功");
    }

    @Override
    public String updateService(ServiceUpdateDTO serviceUpdateDTO) {
        GenericService service = new GenericService();
        BeanUtils.copyProperties(serviceUpdateDTO, service);
        int result = serviceMapper.updateById(service);
        return this.response(result, "更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteServiceById(ServiceDeleteDTO serviceDeleteDTO) {
        GenericService service = new GenericService();
        BeanUtils.copyProperties(serviceDeleteDTO, service);
        QueryWrapper<GenericReport> reportQueryWrapper = new QueryWrapper<>();
        QueryWrapper<ServiceCol> serviceColQueryWrapper = new QueryWrapper<>();
        reportQueryWrapper.eq("service_id", service.getId());
        serviceColQueryWrapper.eq("service_id", service.getId());
        // 根据接口的主键删除报表
        reportService.deleteReportByColumns(reportQueryWrapper);
        // 删除 ServiceCol
        serviceColService.deleteServiceColByColumns(serviceColQueryWrapper);
        int result = serviceMapper.deleteById(service);
        return this.response(result, "删除成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteServiceByColumns(QueryWrapper<GenericService> queryWrapper) {
        queryWrapper.select("id");
        // 获取即将删除报销的主键
        List<GenericService> serviceList = serviceMapper.selectList(queryWrapper);
        if (!serviceList.isEmpty()) {
            List<Integer> ids = new ArrayList<>();
            serviceList.forEach(service -> ids.add(service.getId()));
            if (!ids.isEmpty()) {
                // 删除报表
                reportService.deleteReportByIds(ids);
                // 删除 ServiceCol
                serviceColService.deleteServiceColByIds(ids);
            }
        }
        int i = serviceMapper.delete(queryWrapper);
        if (i > 0) {
            this.deleteCachedKeys();
        }
        return i;
    }

    @Override
    public Integer verifySql(String sql) {
        if (!sql.toLowerCase().contains("select")) {
            return -1;
        }
        List<LinkedHashMap<String, Object>> linkedHashMaps = apiMapper.genericSelect(sql);
        return linkedHashMaps.size();
    }

    @Override
    public Map<String, List<String>> queryTableViewNameList(String databaseName) {
        // MySQL 
        List<String> tableList = apiMapper.selectDatabaseInfo("SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = '" + databaseName + "'");
        List<String> viewList = apiMapper.selectDatabaseInfo("SELECT TABLE_NAME FROM information_schema.VIEWS WHERE TABLE_SCHEMA = '" + databaseName + "'");
        Map<String, List<String>> tableViewMap = new LinkedHashMap<>();
        tableViewMap.put("table", tableList);
        tableViewMap.put("view", viewList);
        return tableViewMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteServiceByIds(List<Integer> ids) {
        // 删除报表
        reportService.deleteReportByIds(ids);
        // 删除 ServiceCol
        serviceColService.deleteServiceColByIds(ids);
        // 删除接口
        int i = serviceMapper.deleteBatchIds(ids);
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

    public List<ServiceVO> getServiceVOList(List<GenericService> serviceList) {
        List<ServiceVO> serviceVOList = new ArrayList<>();
        serviceList.forEach(service -> {
            ServiceVO vo = new ServiceVO();
            BeanUtils.copyProperties(service, vo);
            serviceVOList.add(vo);
        });
        return serviceVOList;
    }

    public String response(int result, String returnMessage) {
        if (result == 0) {
            throw new GenericException(ResponseStatusEnum.FAIL);
        } else {
            this.deleteCachedKeys();
        }
        return returnMessage;
    }
}