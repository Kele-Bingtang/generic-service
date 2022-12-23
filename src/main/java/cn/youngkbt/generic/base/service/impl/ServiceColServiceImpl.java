package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.ServiceColMapper;
import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.exception.ExecuteSqlException;
import cn.youngkbt.generic.http.ResponseStatusEnum;
import cn.youngkbt.generic.utils.SearchUtils;
import cn.youngkbt.generic.utils.SecurityUtils;
import cn.youngkbt.generic.utils.StringUtils;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public List<ServiceCol> queryServiceColByConditions(List<ConditionVo> conditionVos) {
        QueryWrapper<ServiceCol> queryWrapper = SearchUtils.parseWhereSql(conditionVos, ServiceCol.class);
        return serviceColMapper.selectList(queryWrapper);
    }

    @Override
    public List<ServiceCol> queryServiceColList(ServiceCol serviceCol) {
        QueryWrapper<ServiceCol> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(serviceCol);
        return serviceColMapper.selectList(queryWrapper);
    }

    @Override
    public ServiceCol insertServiceCol(ServiceCol serviceCol) {
        int i = serviceColMapper.insert(serviceCol);
        if (i == 0) {
            return null;
        }
        return serviceCol;
    }

    @Override
    public ServiceCol updateServiceCol(ServiceCol serviceCol) {
        int i = serviceColMapper.updateById(serviceCol);
        if (i == 0) {
            return null;
        }
        return serviceCol;
    }

    @Override
    public ServiceCol deleteServiceColById(ServiceCol serviceCol) {
        int i = serviceColMapper.deleteById(serviceCol);
        if (i == 0) {
            return null;
        }
        return serviceCol;
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
        String username = SecurityUtils.getUsername();
        try {
            ResultSetMetaData metaData = this.executeSql(selectSql);
            List<ServiceCol> serviceColList = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                ServiceCol serviceCol = new ServiceCol();
                serviceCol.setTableCol(metaData.getColumnName(i));
                serviceCol.setColType(metaData.getColumnTypeName(i));
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
            return this.saveBatch(serviceColList);
        } catch (SQLException e) {
            throw new ExecuteSqlException(ResponseStatusEnum.SERVICE_SQL_EXCEPTION.getCode(), ResponseStatusEnum.SERVICE_SQL_EXCEPTION.getMessage());
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
            throw new ExecuteSqlException(ResponseStatusEnum.SERVICE_SQL_EXCEPTION.getCode(), ResponseStatusEnum.SERVICE_SQL_EXCEPTION.getMessage());
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

}