package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.GenericApiMapper;
import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.service.GenericApiService;
import cn.youngkbt.generic.base.service.GenericProjectService;
import cn.youngkbt.generic.base.service.GenericServiceService;
import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.exception.GenericException;
import cn.youngkbt.generic.http.ResponseStatusEnum;
import cn.youngkbt.generic.utils.ObjectUtils;
import cn.youngkbt.generic.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 22:59
 * @note
 */
@Service
public class GenericApiServiceImpl implements GenericApiService {
    @Resource
    private GenericProjectService genericProjectService;
    @Resource
    private GenericServiceService genericServiceService;
    @Resource
    private ServiceColService serviceColService;
    @Resource
    private GenericApiMapper genericApiMapper;

    @Override
    public List<LinkedHashMap<String, Object>> queryServiceData(String projectUrl, String serviceUrl, String secretKey, String from, Integer pageNo, Integer pageSize) {
        GenericProject project = genericProjectService.queryGenericOneProjectBySecretKey(secretKey);
        Assert.notNull(project, "项目不存在");
        Assert.isTrue(projectUrl.equals(project.getBaseUrl()), "违法的 baseUrl");
        GenericService genericService = new GenericService();
        genericService.setProjectId(project.getId());
        genericService.setServiceUrl(serviceUrl);
        GenericService service = genericServiceService.queryOneGenericService(genericService);
        Assert.isTrue(service.getStatus() == 1, "该接口已被禁用");
        String selectSql = service.getSelectSql();
        String selectTable = service.getSelectTable();
        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
        if (StringUtils.isNotBlank(selectSql)) {
            // 分页处理
            if (ObjectUtils.isNotEmpty(pageNo) && ObjectUtils.isNotEmpty(pageSize)) {
                if (!selectSql.contains("limit") && !selectSql.contains("LIMIT")) {
                    selectSql += "limit " + (pageNo - 1) * pageSize + ", " + pageSize;
                }
            }
            // 通过 selectSql 拿到数据
            list = genericApiMapper.genericSelect(selectSql);
        } else if (StringUtils.isNotBlank(selectTable)) {
            String databaseName = project.getDatabaseName();
            String sql = "";
            if (ObjectUtils.isNotEmpty(pageNo) && ObjectUtils.isNotEmpty(pageSize)) {
                sql = "SELECT * FROM " + databaseName + "." + selectTable + "limit " + (pageNo - 1) * pageSize + ", " + pageSize;
            } else {
                sql = "SELECT * FROM " + databaseName + "." + selectTable;
            }
            list = genericApiMapper.genericSelect(sql);
        }
        // 获取数据对应的字段配置信息
        ServiceCol serviceCol = new ServiceCol();
        serviceCol.setServiceId(service.getId());
        List<ServiceCol> serviceColList = serviceColService.queryServiceColList(serviceCol);
        return this.processMappingForQuery(serviceColList, list, from);
    }

    @Override
    public String operateGenericApi(String projectUrl, String serviceUrl, String secretKey, HashMap<String, Object> data, JSONArray jsonArray) {
        GenericProject project = genericProjectService.queryGenericOneProjectBySecretKey(secretKey);
        Assert.notNull(project, "项目不存在");
        Assert.isTrue(projectUrl.equals(project.getBaseUrl()), "违法的 baseUrl");
        GenericService genericService = new GenericService();
        genericService.setProjectId(project.getId());
        genericService.setServiceUrl(serviceUrl);
        GenericService service = genericServiceService.queryOneGenericService(genericService);
        // 获取数据对应的字段配置信息
        ServiceCol serviceCol = new ServiceCol();
        serviceCol.setServiceId(service.getId());
        List<ServiceCol> serviceColList = serviceColService.queryServiceColList(serviceCol);
        // x-www-form-urlencoded
        if (data.size() > 0) {
            // submitType 如果在表单里
            String submitType = (String) data.get("submitType");
            if (StringUtils.isNotBlank(submitType)) {
                List<HashMap<String, Object>> dataAndKeyList = this.processMappingForOperate(serviceColList, data, submitType);
                String sql = this.getOperateSql(service, dataAndKeyList, submitType);
                Integer result = genericApiMapper.genericOperate(sql);
                if (result != 0) {
                    return "操作成功";
                }
            } else {
                return "请选择操作类型：insert、update、delete";
            }
        }
        // JSON（支持批量）
        if (ObjectUtils.isNotEmpty(jsonArray)) {

        }
        return null;
    }

    public List<LinkedHashMap<String, Object>> processMappingForQuery(List<ServiceCol> serviceColList, List<LinkedHashMap<String, Object>> list, String from) {
        List<LinkedHashMap<String, Object>> tempList = new ArrayList<>();
        list.forEach(map -> {
            LinkedHashMap<String, Object> tempMap = new LinkedHashMap<>(serviceColList.size());
            for (ServiceCol serviceCol : serviceColList) {
                boolean isMatch = false;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (serviceCol.getTableCol().equals(entry.getKey())) {
                        isMatch = true;
                        Object value = "";
                        // 处理日期格式：2022-12-10T13:32:14.000+00:00 转为 yyyy:MM:dd HH:mm:ss
                        if (entry.getValue() instanceof Timestamp) {
                            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
                            Timestamp time = (Timestamp) (entry.getValue());
                            value = dateFormat.format(time.toLocalDateTime());
                        } else {
                            value = entry.getValue();
                        }
                        if (StringUtils.isNotBlank(from) && from.equals("report")) {
                            // if (serviceCol.getAllowShowInReport() == 1) {
                            tempMap.put(serviceCol.getJsonCol(), value);
                            tempMap.put("_" + serviceCol.getJsonCol() + "GenericReportSetting", serviceCol);
                            // }
                        } else {
                            tempMap.put(serviceCol.getJsonCol(), value);
                        }
                        break;
                    } else {
                        isMatch = false;
                    }
                }
                // serviceColList 存在的，list 里不存在，但是也要添加进去 tempMap
                if (!isMatch) {
                    if (StringUtils.isNotBlank(from) && from.equals("report")) {
                        if (serviceCol.getAllowShowInReport() == 1) {
                            tempMap.put(serviceCol.getJsonCol(), "");
                            tempMap.put("_" + serviceCol.getJsonCol() + "GenericReportSetting", serviceCol);
                        }
                    } else {
                        tempMap.put(serviceCol.getJsonCol(), "");
                    }
                }
            }
            tempList.add(tempMap);
        });
        return tempList;
    }

    /**
     * 单行数据 增删改
     * tempMap：单行数据
     * keyMap：主键数据
     */
    public List<HashMap<String, Object>> processMappingForOperate(List<ServiceCol> serviceColList, HashMap<String, Object> map, String submitType) {
        List<HashMap<String, Object>> list = new ArrayList<>();
        // 单行数据
        HashMap<String, Object> dataMap = new HashMap<>(16);
        // 主键数据
        HashMap<String, Object> keyMap = new HashMap<>(16);
        // 类型数据
        HashMap<String, Object> typeMap = new HashMap<>(16);
        for (ServiceCol serviceCol : serviceColList) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (serviceCol.getJsonCol().equals(entry.getKey())) {
                    // allowInsert、allowUpdate 判断
                    if ("insert".equalsIgnoreCase(submitType) && serviceCol.getAllowInsert() == 0) {
                        break;
                    } else if ("update".equalsIgnoreCase(submitType) && serviceCol.getAllowUpdate() == 0 && serviceCol.getIsWhereKey() == 0) { // 主键不能 break 掉
                        break;
                    }
                    // 1 代表一定添加到 map，2 代表如果值为空，则不添加到 map，0 代表不添加
                    if (serviceCol.getIsWhereKey() == 1) {
                        keyMap.put(serviceCol.getTableCol(), entry.getValue());
                    } else if (serviceCol.getIsWhereKey() == 2 && !"".equals(entry.getValue())) {
                        keyMap.put(serviceCol.getTableCol(), entry.getValue());
                    } else if (serviceCol.getIsWhereKey() == 0) {
                        dataMap.put(serviceCol.getTableCol(), entry.getValue());
                    }
                    typeMap.put(serviceCol.getTableCol(), serviceCol.getColType());
                    break;
                }
            }
        }
        list.add(dataMap);
        list.add(keyMap);
        list.add(typeMap);
        return list;
    }

    public String getOperateSql(GenericService service, List<HashMap<String, Object>> dataAndKeyList, String submitType) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> dataMap = dataAndKeyList.get(0);
        HashMap<String, Object> keyMap = dataAndKeyList.get(1);
        // TODO：执行 insert、update、delete 时候，判断数据的类型来赋值
        HashMap<String, Object> typeMap = dataAndKeyList.get(2);
        if (("insert").equalsIgnoreCase(submitType)) {
            String insertTable = service.getInsertTable();
            if (StringUtils.isBlank(insertTable)) {
                throw new GenericException(ResponseStatusEnum.NO_EXEIT_TABLE);
            } else if (dataMap.size() == 0) {
                throw new GenericException(ResponseStatusEnum.NO_EXEIT_WHERE_DATA);
            }
            sql.append("INSERT INTO ").append(insertTable);
            sql.append("(");
            dataMap.forEach((key, value) -> {
                sql.append(key).append(",");
            });
            sql.deleteCharAt(sql.length() - 1);
            sql.append(") values (");
            dataMap.forEach((key, value) -> {
                sql.append("'").append(value).append("',");
            });
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
        } else if (("update").equalsIgnoreCase(submitType)) {
            String updateTable = service.getUpdateTable();
            if (StringUtils.isBlank(updateTable)) {
                throw new GenericException(ResponseStatusEnum.NO_EXEIT_TABLE);
            } else if (keyMap.size() == 0) {
                throw new GenericException(ResponseStatusEnum.NO_EXEIT_WHERE_KEY);
            } else if (dataMap.size() == 0) {
                throw new GenericException(ResponseStatusEnum.NO_EXEIT_WHERE_DATA);
            }
            sql.append("UPDATE ").append(service.getUpdateTable()).append(" SET ");
            dataMap.forEach((key, value) -> {
                sql.append(key).append(" = '").append(value).append("', ");
            });
            sql.delete(sql.length() - 2, sql.length());
            sql.append(" WHERE ");
            keyMap.forEach((key, value) -> {
                sql.append(key).append(" = '").append(value).append("' and ");
            });
            sql.delete(sql.length() - 5, sql.length());
        } else if (("delete").equalsIgnoreCase(submitType)) {
            String deleteTable = service.getDeleteTable();
            if (StringUtils.isBlank(deleteTable)) {
                throw new GenericException(ResponseStatusEnum.NO_EXEIT_TABLE);
            } else if (keyMap.size() == 0) {
                throw new GenericException(ResponseStatusEnum.NO_EXEIT_WHERE_KEY);
            }
            sql.append("DELETE FROM ").append(service.getDeleteTable());
            sql.append(" WHERE ");
            keyMap.forEach((key, value) -> {
                sql.append(key).append(" = '").append(value).append("' and ");
            });
            sql.delete(sql.length() - 5, sql.length());

        }
        return sql.toString();
    }
}
