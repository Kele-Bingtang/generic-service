package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.dto.service.ServiceQueryDTO;
import cn.youngkbt.generic.base.dto.serviceCol.ServiceColQueryDTO;
import cn.youngkbt.generic.base.mapper.ApiMapper;
import cn.youngkbt.generic.base.model.GenericField;
import cn.youngkbt.generic.base.service.*;
import cn.youngkbt.generic.base.vo.ProjectVO;
import cn.youngkbt.generic.base.vo.ServiceColVO;
import cn.youngkbt.generic.base.vo.ServiceVO;
import cn.youngkbt.generic.exception.GenericException;
import cn.youngkbt.generic.http.ResponseStatusEnum;
import cn.youngkbt.generic.utils.Assert;
import cn.youngkbt.generic.utils.ObjectUtils;
import cn.youngkbt.generic.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 22:59
 * @note
 */
@Service
public class ApiServiceImpl implements ApiService {
    @Resource
    private ProjectService projectService;
    @Resource
    private ServiceService serviceService;
    @Resource
    private ServiceColService serviceColService;
    @Resource
    private FieldService fieldService;
    @Resource
    private ApiMapper apiMapper;

    private static final String AND = " AND ";
    private static final String WHERE = " WHERE ";
    private static final String LIMIT = " LIMIT ";
    private static final String ORDERBY = " ORDER BY ";
    private static final String LIKE = " LIKE ";
    private static final String BETWEEN = " BETWEEN ";

    @Override
    public List<LinkedHashMap<String, Object>> queryServiceData(String projectUrl, String serviceUrl, String secretKey, Map<String, Object> request) {
        ProjectVO projectVO = projectService.queryOneProjectBySecretKey(secretKey);
        ServiceVO serviceVO = this.getServiceVOAndSound(projectUrl, serviceUrl, projectVO);
        Assert.isTrue(serviceVO.getStatus() == 1, "该接口已被禁用");
        // 获取数据对应的字段配置信息
        ServiceColQueryDTO serviceColQueryDTO = new ServiceColQueryDTO();
        serviceColQueryDTO.setServiceId(serviceVO.getId());
        List<ServiceColVO> serviceColVOList = serviceColService.queryServiceColList(serviceColQueryDTO);
        if (serviceColVOList.isEmpty()) {
            return Collections.emptyList();
        }
        // 获取 SQL
        String selectSql = this.getSelectSQL(serviceVO, request, serviceColVOList, projectVO.getDatabaseName());
        // 通过 selectSql 拿到数据
        List<LinkedHashMap<String, Object>> list;
        try {
            list = apiMapper.genericSelect(selectSql);
        } catch (Exception e) {
            throw new GenericException(ResponseStatusEnum.SERVICE_SQL_EXCEPTION);
        }
        List<GenericField> fieldList = fieldService.queryFieldByJsonCol(serviceVO.getId());
        return this.processMappingForQuery(serviceColVOList, list, fieldList, (String) request.get("_from"));
    }

    public String getSelectSQL(ServiceVO serviceVO, Map<String, Object> request, List<ServiceColVO> serviceColVOList, String databaseName) {
        String selectSql = serviceVO.getSelectSql();
        String selectTable = serviceVO.getSelectTable();
        // 其他的都是条件查询，所以去掉
        Map<String, Object> map = new HashMap<>(request);
        map.remove("pageNo");
        map.remove("pageSize");
        map.remove("_from");
        map.remove("token");
        // 查询 sql
        String querySql = this.getQuerySql(map, serviceColVOList);
        String fullQuerySql = querySql.length() > 0 ? WHERE + querySql : "";
        // 分组 sql
        String orderBySql = this.getOrderBySql(serviceColVOList);
        String fullOrderBySql = orderBySql.length() > 0 ? ORDERBY + orderBySql : "";
        // 分页 sql
        String limitSql = this.getLimitSql(request);
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isNotBlank(databaseName)) {
            sql.append("use ").append(databaseName).append(";\n");
        }
        if (StringUtils.isNotBlank(selectSql)) {
            // 查询和分页处理，直接将 selectSql 包起来进行查询和分页
            sql.append("SELECT b.* FROM (").append("SELECT a.* FROM (").append(selectSql).append(") a").append(fullQuerySql).append(fullOrderBySql).append(") b").append(limitSql);
        } else if (StringUtils.isNotBlank(selectTable)) {
            if (StringUtils.isNotBlank(databaseName)) {
                sql.append("SELECT * FROM ").append(databaseName).append(".").append(selectTable).append(fullQuerySql).append(fullOrderBySql).append(limitSql);
            } else {
                sql.append("SELECT * FROM ").append(selectTable).append(fullQuerySql).append(fullOrderBySql).append(limitSql);
            }
        } else {
            throw new GenericException(ResponseStatusEnum.SERVICE_SQL_EXCEPTION);
        }
        return sql.toString();
    }

    public String getQuerySql(Map<String, Object> map, List<ServiceColVO> serviceColVOList) {
        StringBuilder querySql = new StringBuilder();
        if (map.size() > 0) {
            for (ServiceColVO serviceColVO : serviceColVOList) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (serviceColVO.getJsonCol().equals(entry.getKey()) && serviceColVO.getAllowFilter() == 1) {
                        Object value = entry.getValue();
                        if (value instanceof String) {
                            String val = (String) value;
                            String v = "'" + val + "'";
                            if (serviceColVO.getQueryFilter() == 1) {
                                querySql.append(serviceColVO.getTableCol()).append(" = ").append(v).append(AND);
                            } else if (serviceColVO.getQueryFilter() == 2) {
                                querySql.append(serviceColVO.getTableCol()).append(" != ").append(v).append(AND);
                            } else if (serviceColVO.getQueryFilter() == 3) {
                                querySql.append(serviceColVO.getTableCol()).append(LIKE).append("'%").append(val).append("'").append(AND);
                            } else if (serviceColVO.getQueryFilter() == 4) {
                                querySql.append(serviceColVO.getTableCol()).append(LIKE).append("'").append(val).append("%'").append(AND);
                            } else if (serviceColVO.getQueryFilter() == 5) {
                                querySql.append(serviceColVO.getTableCol()).append(LIKE).append("'%").append(val).append("%'").append(AND);
                            } else if (serviceColVO.getQueryFilter() == 6) {
                                querySql.append(serviceColVO.getTableCol()).append(" < ").append(v).append(AND);
                            } else if (serviceColVO.getQueryFilter() == 7) {
                                querySql.append(serviceColVO.getTableCol()).append(" > ").append(v).append(AND);
                            } else if (serviceColVO.getQueryFilter() == 8) {
                                querySql.append(serviceColVO.getTableCol()).append(" <= ").append(v).append(AND);
                            } else if (serviceColVO.getQueryFilter() == 9) {
                                querySql.append(serviceColVO.getTableCol()).append(" >= ").append(v).append(AND);
                            }
                            break;
                        } else if (value instanceof String[]) {
                            String[] v = (String[]) value;
                            if (serviceColVO.getQueryFilter() == 10) {
                                querySql.append(serviceColVO.getTableCol()).append(BETWEEN)
                                        .append("'").append(v[0]).append("'").append(AND)
                                        .append("'").append(v[1]).append("'").append(AND);
                            }
                        }
                    }
                }
            }
            if (querySql.length() > 0) {
                querySql.delete(querySql.length() - AND.length(), querySql.length());
            }
        }
        return querySql.toString();
    }

    public String getOrderBySql(List<ServiceColVO> serviceColVOList) {
        StringBuilder orderBySql = new StringBuilder();
        Stream<ServiceColVO> sortedServiceColVOList = serviceColVOList.stream().sorted(Comparator.comparing(x -> Math.abs(x.getOrderBy())));
        sortedServiceColVOList.forEach(serviceColVO -> {
            Integer orderBy = serviceColVO.getOrderBy();
            if (orderBy > -99 && orderBy < 0) {
                orderBySql.append(serviceColVO.getTableCol()).append(" desc").append(", ");
            } else if (orderBy > 0 && orderBy < 99) {
                orderBySql.append(serviceColVO.getTableCol()).append(" asc").append(", ");
            }
        });
        if (orderBySql.length() > 0) {
            orderBySql.delete(orderBySql.length() - 2, orderBySql.length());
        }
        return orderBySql.toString();
    }

    public String getLimitSql(Map<String, Object> request) {
        StringBuilder limitSql = new StringBuilder();
        String pageNo = (String) request.get("pageNo");
        String pageSize = (String) request.get("pageSize");
        if (ObjectUtils.isNotEmpty(pageNo) && ObjectUtils.isNotEmpty(pageSize)) {
            limitSql.append(LIMIT).append((Integer.parseInt(pageNo) - 1) * Integer.parseInt(pageSize)).append(", ").append(pageSize);
        }
        return limitSql.toString();
    }

    public List<LinkedHashMap<String, Object>> processMappingForQuery(List<ServiceColVO> serviceColList, List<LinkedHashMap<String, Object>> list, List<GenericField> fieldList, String from) {
        List<LinkedHashMap<String, Object>> serviceData = new ArrayList<>();
        list.forEach(oneServiceData -> {
            LinkedHashMap<String, Object> resultData = this.processMapping(serviceColList, oneServiceData, fieldList, from);
            // 处理 parentField，parentField 将包起 Field
            for (GenericField field : fieldList) {
                if (StringUtils.isNotBlank(field.getParentField()) && ObjectUtils.isNotEmpty(resultData.get(field.getField()))) {
                    Map<String, Object> subMap = new LinkedHashMap<>();
                    subMap.put(field.getField(), resultData.get(field.getField()));
                    Map<String, Object> parentMap = (Map<String, Object>) resultData.getOrDefault(field.getParentField(), new LinkedHashMap<>());
                    if (!parentMap.isEmpty()) {
                        parentMap.forEach((k, v) -> subMap.merge(k, v, (v1, v2) -> v1));
                    }
                    resultData.put(field.getParentField(), subMap);
                    // 最后删除 field，只留下 parentField 包起来
                    resultData.remove(field.getField());
                }
            }
            serviceData.add(resultData);
        });
        return serviceData;
    }

    public LinkedHashMap<String, Object> processMapping(List<ServiceColVO> serviceColList, LinkedHashMap<String, Object> map, List<GenericField> fieldList, String from) {
        LinkedHashMap<String, Object> oneData = new LinkedHashMap<>(serviceColList.size());
        for (ServiceColVO serviceCol : serviceColList) {
            // 如果该字段不允许出现在请求，则不添加
            if (serviceCol.getAllowRequest() == 0) {
                continue;
            }
            boolean isMatch = false;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (serviceCol.getTableCol().equals(entry.getKey())) {
                    Object value = this.processOneData(entry);
                    this.addMapDataForQuery(oneData, serviceCol, fieldList, from, value);
                    isMatch = true;
                    break;
                } else {
                    isMatch = false;
                }
            }
            // serviceColList 存在的，list 里不存在，但是也要添加进去 oneData
            if (!isMatch) {
                this.addMapDataForQuery(oneData, serviceCol, fieldList, from, "");
            }
        }
        return oneData;
    }

    public Object processOneData(Map.Entry<String, Object> entry) {
        Object value = "";
        // 处理日期格式：2022-12-10T13:32:14.000+00:00 转为 yyyy:MM:dd HH:mm:ss
        if (entry.getValue() instanceof Timestamp) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
            Timestamp time = (Timestamp) (entry.getValue());
            value = dateFormat.format(time.toLocalDateTime());
        } else {
            value = entry.getValue();
        }
        return value;
    }

    public void addMapDataForQuery(LinkedHashMap<String, Object> oneData, ServiceColVO serviceCol, List<GenericField> fieldList, String from, Object value) {
        boolean isMatch = convertFieldMap(oneData, serviceCol, fieldList, from, value);
        if (!isMatch) {
            oneData.put(serviceCol.getJsonCol(), value);
        }
        if (StringUtils.isNotBlank(from) && from.equals("report")) {
            oneData.put("_" + serviceCol.getJsonCol() + "GenericReportSetting", serviceCol);
        }
    }

    public boolean convertFieldMap(LinkedHashMap<String, Object> map, ServiceColVO serviceCol, List<GenericField> fieldList, String from, Object value) {
        boolean isMatch = false;
        if (!fieldList.isEmpty() && (StringUtils.isBlank(from) || !from.equals("report"))) {
            for (GenericField field : fieldList) {
                if (field.getJsonCol().contains(serviceCol.getJsonCol())) {
                    Map<String, Object> fieldMap = (Map<String, Object>) map.getOrDefault(field.getField(), new LinkedHashMap<>());
                    fieldMap.put(serviceCol.getJsonCol(), value);
                    map.put(field.getField(), fieldMap);
                    isMatch = true;
                    break;
                } else {
                    isMatch = false;
                }
            }
        }
        return isMatch;
    }


    @Override
    public String operateGenericApi(String projectUrl, String serviceUrl, String secretKey, Map<String, Object> data, JSONArray jsonArray) {
        ProjectVO projectVO = projectService.queryOneProjectBySecretKey(secretKey);
        ServiceVO serviceVO = this.getServiceVOAndSound(projectUrl, serviceUrl, projectVO);
        Assert.isTrue(serviceVO.getStatus() == 1, "该接口已被禁用");
        // 获取数据对应的字段配置信息
        ServiceColQueryDTO serviceCol = new ServiceColQueryDTO();
        serviceCol.setServiceId(serviceVO.getId());
        List<ServiceColVO> serviceColVOList = serviceColService.queryServiceColList(serviceCol);
        // x-www-form-urlencoded
        if (data.size() > 0) {
            // submitType 如果在表单里
            String submitType = (String) data.get("submitType");
            if (StringUtils.isNotBlank(submitType)) {
                List<HashMap<String, Object>> dataAndKeyList = this.processMappingForOperate(serviceColVOList, data, submitType);
                String sql = this.getOperateSql(serviceVO, dataAndKeyList, submitType);
                Integer result = apiMapper.genericOperate(sql);
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

    /**
     * 单行数据 增删改
     * tempMap：单行数据
     * keyMap：主键数据
     */
    public List<HashMap<String, Object>> processMappingForOperate(List<ServiceColVO> serviceColList, Map<String, Object> data, String submitType) {
        List<HashMap<String, Object>> list = new ArrayList<>();
        // 单行数据
        HashMap<String, Object> dataMap = new HashMap<>(16);
        // 主键数据
        HashMap<String, Object> keyMap = new HashMap<>(16);
        // 类型数据
        HashMap<String, Object> typeMap = new HashMap<>(16);
        // 默认值数据
        HashMap<String, Object> defaultValueMap = new HashMap<>(16);
        boolean isMatch = false;
        for (ServiceColVO serviceCol : serviceColList) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (serviceCol.getJsonCol().equals(entry.getKey())) {
                    // allowInsert、allowUpdate 判断
                    if (("insert".equalsIgnoreCase(submitType) && serviceCol.getAllowInsert() == 0) || ("update".equalsIgnoreCase(submitType) && serviceCol.getAllowUpdate() == 0 && serviceCol.getIsWhereKey() == 0)) {
                        break;
                    }
                    this.addMapDataForOperator(serviceCol, entry, keyMap, dataMap, typeMap);
                    isMatch = true;
                    break;
                } else {
                    isMatch = false;
                }
            }
            if (!isMatch && StringUtils.isNotBlank(serviceCol.getDefaultValue()) && serviceCol.getIsWhereKey() == 0) {
                defaultValueMap.put(serviceCol.getTableCol(), serviceCol.getDefaultValue());
            }
        }
        list.add(dataMap);
        list.add(keyMap);
        list.add(typeMap);
        list.add(defaultValueMap);
        return list;
    }

    public void addMapDataForOperator(ServiceColVO serviceCol, Map.Entry<String, Object> entry, HashMap<String, Object> keyMap, HashMap<String, Object> dataMap, HashMap<String, Object> typeMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        Object value = entry.getValue();
        String v = "";
        if (value instanceof String) {
            v = (String) value;
        } else if (value instanceof String[]) {
            String[] s = ((String[]) value);
            if (s.length > 0) {
                try {
                    // 转 JSON 字符串
                    v = objectMapper.writeValueAsString(s);
                } catch (JsonProcessingException e) {
                    throw new GenericException(ResponseStatusEnum.PARAMS_ERROR);
                }
            }
        }
        // 值不能为空
        if (StringUtils.isNotBlank(v)) {
            // 1 代表一定添加到 keyMap，2 代表如果值为空，则不添加到 keyMap，0 代表不添加
            if (serviceCol.getIsWhereKey() == 1) {
                keyMap.put(serviceCol.getTableCol(), v);
            } else if (serviceCol.getIsWhereKey() == 2 && !"".equals(v)) {
                keyMap.put(serviceCol.getTableCol(), v);
            } else if (serviceCol.getIsWhereKey() == 0) {
                dataMap.put(serviceCol.getTableCol(), v);
            }
        }
        typeMap.put(serviceCol.getTableCol(), serviceCol.getColType());
    }

    public String getOperateSql(ServiceVO service, List<HashMap<String, Object>> dataAndKeyList, String submitType) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> dataMap = dataAndKeyList.get(0);
        HashMap<String, Object> keyMap = dataAndKeyList.get(1);
        // TODO：执行 insert、update、delete 时候，判断数据的类型来赋值
        HashMap<String, Object> typeMap = dataAndKeyList.get(2);
        HashMap<String, Object> defaultValueMap = dataAndKeyList.get(3);
        if (("insert").equalsIgnoreCase(submitType)) {
            defaultValueMap.forEach((key, value) -> dataMap.merge(key, value, (v1, v2) -> v1));
            String insertTable = service.getInsertTable();
            Assert.notBlank(insertTable, ResponseStatusEnum.NO_EXIST_TABLE);
            Assert.isTrue(dataMap.size() != 0, ResponseStatusEnum.NO_EXIST_WHERE_DATA);
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
            Assert.notBlank(updateTable, ResponseStatusEnum.NO_EXIST_TABLE);
            Assert.isTrue(keyMap.size() != 0, ResponseStatusEnum.NO_EXIST_WHERE_KEY);
            Assert.isTrue(dataMap.size() != 0, ResponseStatusEnum.NO_EXIST_WHERE_DATA);
            sql.append("UPDATE ").append(service.getUpdateTable()).append(" SET ");
            dataMap.forEach((key, value) -> {
                sql.append(key).append(" = '").append(value).append("', ");
            });
            sql.delete(sql.length() - 2, sql.length());
            sql.append(WHERE);
            keyMap.forEach((key, value) -> {
                sql.append(key).append(" = '").append(value).append("'").append(AND);
            });
            sql.delete(sql.length() - AND.length(), sql.length());
        } else if (("delete").equalsIgnoreCase(submitType)) {
            String deleteTable = service.getDeleteTable();
            Assert.notBlank(deleteTable, ResponseStatusEnum.NO_EXIST_TABLE);
            Assert.isTrue(keyMap.size() != 0, ResponseStatusEnum.NO_EXIST_WHERE_KEY);
            sql.append("DELETE FROM ").append(service.getDeleteTable());
            sql.append(WHERE);
            keyMap.forEach((key, value) -> {
                sql.append(key).append(" = '").append(value).append("'").append(AND);
            });
            sql.delete(sql.length() - AND.length(), sql.length());
        }
        return sql.toString();
    }

    public ServiceVO getServiceVOAndSound(String projectUrl, String serviceUrl, ProjectVO projectVO) {
        Assert.notNull(projectVO, "项目不存在");
        Assert.isTrue(projectUrl.equals(projectVO.getBaseUrl()), "违法的 baseUrl");
        ServiceQueryDTO serviceQueryDTO = new ServiceQueryDTO();
        serviceQueryDTO.setProjectId(projectVO.getId());
        serviceQueryDTO.setServiceUrl(serviceUrl);
        return serviceService.queryOneGenericService(serviceQueryDTO);
    }
}
