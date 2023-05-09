package cn.youngkbt.generic.service.impl;

import cn.youngkbt.generic.common.dto.service.ServiceQueryDTO;
import cn.youngkbt.generic.common.dto.serviceCol.ServiceColQueryDTO;
import cn.youngkbt.generic.common.exception.GenericException;
import cn.youngkbt.generic.common.http.ResponseStatusEnum;
import cn.youngkbt.generic.common.model.GenericField;
import cn.youngkbt.generic.common.model.GenericProject;
import cn.youngkbt.generic.common.model.GenericService;
import cn.youngkbt.generic.common.model.ServiceCol;
import cn.youngkbt.generic.common.utils.Assert;
import cn.youngkbt.generic.common.utils.ObjectUtils;
import cn.youngkbt.generic.common.utils.StringUtils;
import cn.youngkbt.generic.mapper.ApiMapper;
import cn.youngkbt.generic.service.*;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 22:59
 * @note Generic API 服务
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
    private static final String VALUE = "value";
    private static final String LABEL = "label";
    private GenericProject project;

    @Override
    public List<LinkedHashMap<String, Object>> queryServiceData(String projectUrl, String serviceUrl, String secretKey, Map<String, Object> request) {
        GenericProject project = projectService.queryOneProjectBySecretKey(secretKey);
        this.project = project;
        GenericService service = this.getServiceAndSound(projectUrl, serviceUrl, project);
        Assert.isTrue(service.getStatus() == 1, "该接口已被禁用");
        // 获取数据对应的字段配置信息
        ServiceColQueryDTO serviceColQueryDTO = new ServiceColQueryDTO();
        serviceColQueryDTO.setServiceId(service.getId());
        List<ServiceCol> serviceColList = serviceColService.queryServiceColList(serviceColQueryDTO);
        if (serviceColList.isEmpty()) {
            return Collections.emptyList();
        }
        // 获取 SQL
        String selectSql = this.getSelectSQL(service, request, serviceColList, project.getDatabaseName());
        // 通过 selectSql 拿到数据
        List<LinkedHashMap<String, Object>> list;
        try {
            list = apiMapper.genericSelect(selectSql);
        } catch (Exception e) {
            throw new GenericException(ResponseStatusEnum.SERVICE_SQL_EXCEPTION);
        }
        List<GenericField> fieldList = fieldService.queryFieldByJsonCol(service.getId());
        return this.processMappingForQuery(serviceColList, list, fieldList, (String) request.get("_from"));
    }

    /**
     * 获取 select sql
     * @param service 接口
     * @param request 请求对象
     * @param serviceColList 字段集合
     * @param databaseName 数据库名
     * @return select sql
     */
    public String getSelectSQL(GenericService service, Map<String, Object> request, List<ServiceCol> serviceColList, String databaseName) {
        String selectSql = service.getSelectSql();
        String selectTable = service.getSelectTable();
        // 其他的都是条件查询，所以去掉
        Map<String, Object> map = new HashMap<>(request);
        map.remove("pageNo");
        map.remove("pageSize");
        map.remove("_from");
        map.remove("token");
        // 查询 sql
        String querySql = this.getQuerySql(map, serviceColList);
        String fullQuerySql = querySql.length() > 0 ? WHERE + querySql : "";
        // 分组 sql
        String orderBySql = this.getOrderBySql(serviceColList);
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

    /**
     * 获取 where 后的查询 sql
     * @param map 字段数据
     * @param serviceColVOList 字段集合
     * @return where 后的查询 sql
     */
    public String getQuerySql(Map<String, Object> map, List<ServiceCol> serviceColVOList) {
        StringBuilder querySql = new StringBuilder();
        if (map.size() > 0) {
            for (ServiceCol serviceColVO : serviceColVOList) {
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
                            // 数组就是时间范围
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

    /**
     * 获取 order by sql
     * @param serviceColVOList 字段结婚
     * @return order by sql
     */
    public String getOrderBySql(List<ServiceCol> serviceColVOList) {
        StringBuilder orderBySql = new StringBuilder();
        Stream<ServiceCol> serviceColStream = serviceColVOList.stream().sorted(Comparator.comparing(x -> Math.abs(x.getOrderBy())));
        serviceColStream.forEach(serviceColVO -> {
            Integer orderBy = serviceColVO.getOrderBy();
            // Order by 顺序：0 - 99
            if (orderBy > -99 && orderBy < 0) {
                orderBySql.append("'").append(serviceColVO.getTableCol()).append("'").append(" desc").append(", ");
            } else if (orderBy > 0 && orderBy < 99) {
                orderBySql.append("'").append(serviceColVO.getTableCol()).append("'").append(" asc").append(", ");
            }
        });
        // 去除结尾的 , 
        if (orderBySql.length() > 0) {
            orderBySql.delete(orderBySql.length() - 2, orderBySql.length());
        }
        return orderBySql.toString();
    }

    /**
     * 获取 limit sql
     * @param request 请求对象
     * @return limit sql
     */
    public String getLimitSql(Map<String, Object> request) {
        StringBuilder limitSql = new StringBuilder();
        String pageNo = (String) request.get("pageNo");
        String pageSize = (String) request.get("pageSize");
        if (ObjectUtils.isNotEmpty(pageNo) && ObjectUtils.isNotEmpty(pageSize)) {
            limitSql.append(LIMIT).append((Integer.parseInt(pageNo) - 1) * Integer.parseInt(pageSize)).append(", ").append(pageSize);
        }
        return limitSql.toString();
    }

    /**
     * 处理字段直接的映射关系和属性
     * @param serviceColList 字段集合
     * @param list 查询的数据
     * @param fieldList 属性集合
     * @param from 来自哪里
     * @return 字段直接的映射关系
     */
    public List<LinkedHashMap<String, Object>> processMappingForQuery(List<ServiceCol> serviceColList, List<LinkedHashMap<String, Object>> list, List<GenericField> fieldList, String from) {
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

    /**
     * 处理字段直接的映射关系
     * @param serviceColList 字段集合
     * @param map 查询的一条数据
     * @param fieldList 属性集合
     * @param from 来自哪里
     * @return 字段直接的映射关系
     */
    public LinkedHashMap<String, Object> processMapping(List<ServiceCol> serviceColList, LinkedHashMap<String, Object> map, List<GenericField> fieldList, String from) {
        LinkedHashMap<String, Object> oneData = new LinkedHashMap<>(serviceColList.size());
        for (ServiceCol serviceCol : serviceColList) {
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

    /**
     * 添加一条处理好的数据
     * @param entry 数据
     * @return 一条处理好的数据
     */
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

    public void addMapDataForQuery(LinkedHashMap<String, Object> oneData, ServiceCol serviceCol, List<GenericField> fieldList, String from, Object value) {
        boolean isMatch = convertFieldMap(oneData, serviceCol, fieldList, from, value);
        if (!isMatch) {
            oneData.put(serviceCol.getJsonCol(), value);
        }
        // 报表配置
        if (StringUtils.isNotBlank(from) && from.equals("report")) {
            oneData.put("_" + serviceCol.getJsonCol() + "GenericReportSetting", serviceCol);
            List<LinkedHashMap<String, Object>> dropdownResult = this.processDropdown(serviceCol);
            // 下拉值
            if(!dropdownResult.isEmpty()) {
                oneData.put("_" + serviceCol.getJsonCol() + "Dropdown", dropdownResult);
            }
        }
    }

    /**
     * 处理属性的映射关系
     * @param map 数据
     * @param serviceCol 字段
     * @param fieldList 属性
     * @param from 来自哪里
     * @param value 值
     * @return 映射后的属性
     */
    public boolean convertFieldMap(LinkedHashMap<String, Object> map, ServiceCol serviceCol, List<GenericField> fieldList, String from, Object value) {
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

    /**
     * 处理下拉值
     * @param serviceCol 字段
     * @return 处理完的下拉值
     */
    public List<LinkedHashMap<String, Object>> processDropdown(ServiceCol serviceCol) {
        List<LinkedHashMap<String, Object>> dropdownList = new ArrayList<>();
        String dropdownValue = serviceCol.getDropdownValue();
        String dropdownService = serviceCol.getDropdownService();
        String dropdownSql = serviceCol.getDropdownSql();
        // 自定义下拉
        if (StringUtils.isNotBlank(dropdownValue)) {
            try {
                dropdownList.addAll(new ObjectMapper().readValue(dropdownValue, List.class));
            } catch (JsonProcessingException e) {
                throw new GenericException(ResponseStatusEnum.DROPDOWN_ERROR);
            }
        }
        // 接口下拉
        if (StringUtils.isNotBlank(serviceCol.getDropdownService())) {
            String[] split = dropdownService.split("-");
            Assert.isTrue(split.length == 3, "接口下拉值异常");
            String serviceId = split[0];
            GenericService service = serviceService.queryServiceById(Integer.valueOf(serviceId));
            ServiceColQueryDTO serviceColQueryDTO = new ServiceColQueryDTO();
            serviceColQueryDTO.setServiceId(Integer.valueOf(serviceId));
            List<ServiceCol> serviceColList = serviceColService.queryServiceColList(serviceColQueryDTO);
            AtomicReference<String> valueCol = new AtomicReference<>("");
            AtomicReference<String> LabelCol = new AtomicReference<>("");
            serviceColList.forEach(col -> {
                if (col.getId().equals(Integer.valueOf(split[1]))) {
                    valueCol.set(col.getTableCol());
                }
                if (col.getId().equals(Integer.valueOf(split[2]))) {
                    LabelCol.set(col.getTableCol());
                }
            });
            service.setSelectSql("SELECT " + valueCol + " as value, " + LabelCol + " as label FROM (" + service.getSelectSql() + ") s");
            String selectSQL = this.getSelectSQL(service, new LinkedHashMap<>(), serviceColList, this.project.getDatabaseName());
            if (StringUtils.isNotBlank(selectSQL)) {
                List<LinkedHashMap<String, Object>> serviceResult = apiMapper.genericSelect(selectSQL);
                // 过滤为空的内容
                dropdownList.addAll(serviceResult);
            }
        }
        // sql下拉
        if (StringUtils.isNotBlank(dropdownSql)) {
            List<LinkedHashMap<String, Object>> sqlResult = apiMapper.genericSelect(dropdownSql);
            List<LinkedHashMap<String, Object>> result = new ArrayList<>();
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            if (sqlResult.size() == 1) {
                Object value = sqlResult.get(0).values().toArray()[0];
                linkedHashMap.put(VALUE, value);
                linkedHashMap.put(LABEL, value);
                result.add(linkedHashMap);
            } else if (sqlResult.size() == 2) {
                Object[] keyArray = sqlResult.get(0).keySet().toArray();
                Object[] valueArray = sqlResult.get(0).values().toArray();
                if (!keyArray[0].equals(VALUE) && !keyArray[1].equals(LABEL)) {
                    linkedHashMap.put(VALUE, valueArray[0]);
                    linkedHashMap.put(LABEL, valueArray[1]);
                } else if (!keyArray[0].equals(VALUE) && keyArray[1].equals(LABEL)) {
                    linkedHashMap.put(VALUE, valueArray[0]);
                } else if (keyArray[0].equals(VALUE) && !keyArray[1].equals(LABEL)) {
                    linkedHashMap.put(LABEL, valueArray[1]);
                } else {
                    result = sqlResult;
                }
            } else if (sqlResult.size() > 2) {
                Object[] valueArray = sqlResult.get(0).values().toArray();
                linkedHashMap.put(VALUE, valueArray[0]);
                linkedHashMap.put(LABEL, valueArray[1]);
                result.add(linkedHashMap);
            }
            dropdownList.addAll(result);
        }
        return dropdownList;
    }

    @Override
    public String operateGenericApi(String projectUrl, String serviceUrl, String secretKey, Map<String, Object> data, JSONArray jsonArray) {
        GenericProject project = projectService.queryOneProjectBySecretKey(secretKey);
        GenericService service = this.getServiceAndSound(projectUrl, serviceUrl, project);
        Assert.isTrue(service.getStatus() == 1, "该接口已被禁用");
        // 获取数据对应的字段配置信息
        ServiceColQueryDTO serviceCol = new ServiceColQueryDTO();
        serviceCol.setServiceId(service.getId());
        List<ServiceCol> serviceColVOList = serviceColService.queryServiceColList(serviceCol);
        // x-www-form-urlencoded
        if (data.size() > 0) {
            // submitType 如果在表单里
            String submitType = (String) data.get("submitType");
            if (StringUtils.isNotBlank(submitType)) {
                List<HashMap<String, Object>> dataAndKeyList = this.processMappingForOperate(serviceColVOList, data, submitType);
                String sql = this.getOperateSql(service, dataAndKeyList, submitType, project.getDatabaseName());
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
    public List<HashMap<String, Object>> processMappingForOperate(List<ServiceCol> serviceColList, Map<String, Object> data, String submitType) {
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
        for (ServiceCol serviceCol : serviceColList) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (serviceCol.getJsonCol().equals(entry.getKey())) {
                    // allowInsert、allowUpdate 判断
                    if (("insert".equalsIgnoreCase(submitType) && serviceCol.getAllowInsert() == 0) ||
                            ("update".equalsIgnoreCase(submitType) && serviceCol.getAllowUpdate() == 0 && serviceCol.getIsWhereKey() == 0)) {
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

    /**
     * 处理增删改的映射关系
     */
    public void addMapDataForOperator(ServiceCol serviceCol, Map.Entry<String, Object> entry, HashMap<String, Object> keyMap, HashMap<String, Object> dataMap, HashMap<String, Object> typeMap) {
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

    /**
     * 获取执行增删改的 sql
     * @param service 接口
     * @param dataAndKeyList sql 需要的字段名
     * @param submitType 增删改类型
     * @return 执行增删改的 sql
     */
    public String getOperateSql(GenericService service, List<HashMap<String, Object>> dataAndKeyList, String submitType, String databaseName) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> dataMap = dataAndKeyList.get(0);
        HashMap<String, Object> keyMap = dataAndKeyList.get(1);
        // TODO：执行 insert、update、delete 时候，判断数据的类型来赋值
        HashMap<String, Object> typeMap = dataAndKeyList.get(2);
        HashMap<String, Object> defaultValueMap = dataAndKeyList.get(3);
        if (StringUtils.isNotBlank(databaseName)) {
            sql.append("use ").append(databaseName).append(";\n");
        }
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

    /**
     * 根据项目 id 和接口 url 获取一条接口
     * @param projectUrl 项目 url
     * @param serviceUrl 接口 url
     * @param project 项目
     * @return 一条接口
     */
    public GenericService getServiceAndSound(String projectUrl, String serviceUrl, GenericProject project) {
        Assert.notNull(project, "项目不存在");
        Assert.isTrue(projectUrl.equals(project.getBaseUrl()), "违法的 baseUrl");
        ServiceQueryDTO serviceQueryDTO = new ServiceQueryDTO();
        serviceQueryDTO.setProjectId(project.getId());
        serviceQueryDTO.setServiceUrl(serviceUrl);
        return serviceService.queryOneGenericService(serviceQueryDTO);
    }
}
