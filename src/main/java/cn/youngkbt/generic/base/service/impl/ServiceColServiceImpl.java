package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.dto.serviceCol.*;
import cn.youngkbt.generic.base.mapper.ServiceColMapper;
import cn.youngkbt.generic.base.mapper.ServiceMapper;
import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.model.UserProject;
import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.base.service.UserProjectService;
import cn.youngkbt.generic.base.vo.ServiceColVO;
import cn.youngkbt.generic.exception.GenericException;
import cn.youngkbt.generic.http.ResponseStatusEnum;
import cn.youngkbt.generic.utils.Assert;
import cn.youngkbt.generic.utils.ObjectUtils;
import cn.youngkbt.generic.utils.SecurityUtils;
import cn.youngkbt.generic.utils.StringUtils;
import cn.youngkbt.generic.valid.ValidList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@Service
public class ServiceColServiceImpl extends ServiceImpl<ServiceColMapper, ServiceCol> implements ServiceColService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ServiceColMapper serviceColMapper;
    @Resource
    private ServiceMapper serviceMapper;
    @Resource
    private UserProjectService userProjectService;
    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<ServiceColVO> queryServiceColList(ServiceColQueryDTO serviceColQueryDTO) {
        ServiceCol serviceCol = new ServiceCol();
        BeanUtils.copyProperties(serviceColQueryDTO, serviceCol);
        QueryWrapper<ServiceCol> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        // 根据 display_seq 升序
        queryWrapper.setEntity(serviceCol).orderByAsc("display_seq");
        List<ServiceCol> serviceColList = serviceColMapper.selectList(queryWrapper);
        return this.getServiceColVOList(serviceColList);
    }

    @Override
    public List<ServiceColVO> queryServiceColListPage(IPage<ServiceCol> page, ServiceColQueryDTO serviceColQueryDTO) {
        // 判断查询的是自己项目的 serviceCol
        Integer serviceId = serviceColQueryDTO.getServiceId();
        GenericService genericService = serviceMapper.selectById(serviceId);
        Integer projectId = genericService.getProjectId();
        Assert.notNull(projectId, "projectId is null");
        QueryWrapper<UserProject> qw = new QueryWrapper<>();
        qw.eq("project_id", projectId).eq("username", SecurityUtils.getUsername());
        UserProject userProject = userProjectService.getOne(qw);
        if (ObjectUtils.isEmpty(userProject)) {
            return Collections.emptyList();
        }
        // 查询自己的 serviceCol
        ServiceCol serviceCol = new ServiceCol();
        BeanUtils.copyProperties(serviceColQueryDTO, serviceCol);
        QueryWrapper<ServiceCol> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(serviceCol);
        try {
            IPage<ServiceCol> serviceColIPage = serviceColMapper.selectPage(page, queryWrapper);
            return this.getServiceColVOList(serviceColIPage.getRecords());
        } catch (Exception e) {
            throw new GenericException(ResponseStatusEnum.CONDITION_SQL_ERROR);
        }
    }

    @Override
    public String insertServiceCol(ServiceColInsertDTO serviceColInsertDTO) {
        // 先判断是否存在字段
        QueryWrapper<ServiceCol> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("table_col", serviceColInsertDTO.getTableCol()).or().eq("json_col", serviceColInsertDTO.getJsonCol());
        ServiceCol sc = serviceColMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(sc) && sc.getId() > 0) {
            throw new GenericException(ResponseStatusEnum.SERVICE_COL_EXEIT);
        }
        ServiceCol serviceCol = new ServiceCol();
        BeanUtils.copyProperties(serviceColInsertDTO, serviceCol);
        int result = serviceColMapper.insert(serviceCol);
        return this.response(result, "插入成功");
    }

    @Override
    public String updateServiceCol(ServiceColUpdateDTO serviceColUpdateDTO) {
        ServiceCol serviceCol = new ServiceCol();
        BeanUtils.copyProperties(serviceColUpdateDTO, serviceCol);
        int result = serviceColMapper.updateById(serviceCol);
        return this.response(result, "修改成功");
    }

    @Override
    public String updateBatchServiceCol(ServiceColBatchUpdateDTO batchUpdateDTO) {
        ValidList<String> jsonColList = batchUpdateDTO.getJsonColList();
        // 转换成 MP 需要的批量更新的格式
        List<ServiceCol> serviceColList = new ArrayList<>();
        jsonColList.forEach(jsonCol -> {
            ServiceCol serviceCol = new ServiceCol();
            serviceCol.setJsonCol(jsonCol);
            if (ObjectUtils.isNotEmpty(batchUpdateDTO.getAllowInsert())) {
                serviceCol.setAllowInsert(batchUpdateDTO.getAllowInsert());
            }
            if (ObjectUtils.isNotEmpty(batchUpdateDTO.getAllowUpdate())) {
                serviceCol.setAllowUpdate(batchUpdateDTO.getAllowUpdate());
            }
            if (ObjectUtils.isNotEmpty(batchUpdateDTO.getAllowFilter())) {
                serviceCol.setAllowFilter(batchUpdateDTO.getAllowFilter());
            }
            if (ObjectUtils.isNotEmpty(batchUpdateDTO.getAllowRequest())) {
                serviceCol.setAllowRequest(batchUpdateDTO.getAllowRequest());
            }
            serviceColList.add(serviceCol);
        });
        boolean result = this.updateBatchByColumn(serviceColList, serviceCol -> {
            LambdaUpdateWrapper<ServiceCol> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(ServiceCol::getJsonCol, serviceCol.getJsonCol());
            return updateWrapper;
        });
        if (result) {
            return "修改成功";
        } else {
            return "修改失败";
        }
    }

    public boolean updateBatchByColumn(Collection<ServiceCol> entityList, Function<ServiceCol, LambdaUpdateWrapper<ServiceCol>> queryWrapperFunction) {
        String sqlStatement = this.getSqlStatement(SqlMethod.UPDATE);
        return executeBatch(entityList, (sqlSession, entity) -> {
            Map<String, Object> param = CollectionUtils.newHashMapWithExpectedSize(8);
            param.put(Constants.ENTITY, entity);
            param.put(Constants.WRAPPER, queryWrapperFunction.apply(entity));
            sqlSession.update(sqlStatement, param);
        });
    }

    @Override
    public String deleteServiceColById(ServiceColDeleteDTO serviceColDeleteDTO) {
        ServiceCol serviceCol = new ServiceCol();
        BeanUtils.copyProperties(serviceColDeleteDTO, serviceCol);
        int result = serviceColMapper.deleteById(serviceCol);
        return this.response(result, "删除成功");
    }

    @Override
    public int deleteServiceColByColumns(QueryWrapper<ServiceCol> queryWrapper) {
        return serviceColMapper.delete(queryWrapper);
    }

    @Override
    public int deleteServiceColByIds(List<Integer> ids) {
        return serviceColMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean queryColumnInfoAndInsert(Integer serviceId, String selectSql) {
        if (StringUtils.isBlank(selectSql)) {
            return false;
        }
        List<ServiceCol> serviceColList = this.queryColumnInfo(serviceId, selectSql);
        return this.saveBatch(serviceColList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer queryColumnInfoAndUpdate(Integer serviceId, String selectSql) {
        if (StringUtils.isBlank(selectSql)) {
            return -1;
        }
        QueryWrapper<ServiceCol> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_id", serviceId);
        List<ServiceCol> odlServiceCols = serviceColMapper.selectList(queryWrapper);
        List<ServiceCol> newServiceColList = this.queryColumnInfo(serviceId, selectSql);
        List<ServiceCol> union = newServiceColList.stream().filter(e -> odlServiceCols.contains(e)).collect(Collectors.toList());
        List<ServiceCol> difference = newServiceColList.stream().filter(e -> !odlServiceCols.contains(e)).collect(Collectors.toList());
        if (difference.isEmpty()) {
            return 0;
        }
        this.saveBatch(difference);
        return difference.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer queryColumnInfoAndDelete(Integer serviceId, String selectSql) {
        if (StringUtils.isBlank(selectSql)) {
            return -1;
        }
        QueryWrapper<ServiceCol> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_id", serviceId);
        List<ServiceCol> odlServiceCols = serviceColMapper.selectList(queryWrapper);
        List<ServiceCol> newServiceColList = this.queryColumnInfo(serviceId, selectSql);
        List<Integer> ids = odlServiceCols.stream().filter(e -> !newServiceColList.contains(e)).map(ServiceCol::getId).collect(Collectors.toList());
        if (ids.isEmpty()) {
            return 0;
        }
        this.removeBatchByIds(ids);
        return ids.size();
    }

    public List<ServiceCol> queryColumnInfo(Integer serviceId, String selectSql) {
        String username = SecurityUtils.getUsername();
        try {
            ResultSetMetaData metaData = this.executeSql(selectSql);
            List<ServiceCol> serviceColList = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                ServiceCol serviceCol = new ServiceCol();
                serviceCol.setTableCol(metaData.getColumnName(i));
                String columnType = this.getTypeNameByColumn(metaData.getColumnTypeName(i).toLowerCase());
                serviceCol.setColType(columnType);
                serviceCol.setColLength(metaData.getPrecision(i));
                // 转为小驼峰
                String lowerCamelCase = StringUtils.columnToLowerCamelCase(metaData.getColumnName(i));
                serviceCol.setJsonCol(lowerCamelCase);
                serviceCol.setReportCol(lowerCamelCase);
                serviceCol.setServiceId(serviceId);
                serviceCol.setCreateUser(username);
                serviceCol.setModifyUser(username);
                serviceColList.add(serviceCol);
            }
            return serviceColList;
        } catch (SQLException e) {
            throw new GenericException(ResponseStatusEnum.SERVICE_SQL_EXCEPTION);
        }
    }

    @Override
    public ResultSetMetaData executeSql(String selectSql) {
        SqlSession sqlSession = this.getNativeSqlSession();
        Connection connection = sqlSession.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSetMetaData metaData = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectSql);
            metaData = resultSet.getMetaData();
        } catch (SQLException e) {
            throw new GenericException(ResponseStatusEnum.SERVICE_SQL_EXCEPTION);
        } finally {
            if (null != sqlSession) {
                closeNativeSqlSession(sqlSession);
            }
            try {
                if (null != statement) {
                    statement.close();
                }
                if (null != resultSet) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return metaData;
    }

    protected SqlSession getNativeSqlSession() {
        return SqlSessionUtils.getSqlSession(sqlSessionTemplate.getSqlSessionFactory(),
                sqlSessionTemplate.getExecutorType(), sqlSessionTemplate.getPersistenceExceptionTranslator());
    }

    protected void closeNativeSqlSession(SqlSession sqlSession) {
        SqlSessionUtils.closeSqlSession(sqlSession, sqlSessionTemplate.getSqlSessionFactory());
    }

    protected String getTypeNameByColumn(String columnTypeName) {
        if (columnTypeName.contains("int") || columnTypeName.equals("number")) {
            return "Integer";
        } else if (columnTypeName.contains("double")) {
            return "Double";
        } else if (columnTypeName.contains("float")) {
            return "Float";
        } else if (columnTypeName.equals("date")) {
            return "Date";
        } else if (columnTypeName.equals("dateTime")) {
            return "DateTime";
        } else if (columnTypeName.equals("timestamp")) {
            return "TimeStamp";
        } else if (columnTypeName.contains("blob")) {
            return "Blob";
        } else if (columnTypeName.contains("text")) {
            return "Text";
        } else {
            return "String";
        }
    }

    public List<ServiceColVO> getServiceColVOList(List<ServiceCol> serviceColList) {
        List<ServiceColVO> serviceColVOList = new ArrayList<>();
        serviceColList.forEach(serviceCol -> {
            ServiceColVO vo = new ServiceColVO();
            BeanUtils.copyProperties(serviceCol, vo);
            serviceColVOList.add(vo);
        });
        return serviceColVOList;
    }

    public String response(int result, String returnMessage) {
        if (result == 0) {
            throw new GenericException(ResponseStatusEnum.FAIL);
        }
        return returnMessage;
    }

}