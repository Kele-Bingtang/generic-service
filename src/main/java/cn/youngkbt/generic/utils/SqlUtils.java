package cn.youngkbt.generic.utils;

import cn.youngkbt.generic.base.mapper.GenericApiMapper;
import cn.youngkbt.generic.config.ApplicationContextHelper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 23:31
 * @note
 */
public class SqlUtils {

    private static GenericApiMapper genericApiMapper = ApplicationContextHelper.getBeanByClass(GenericApiMapper.class);

    /**
     * 传入查询 sql、实体类对象的 class，返回 list 实体类集合
     **/
    public static <T> List<T> getResult(String sql, Class<T> pojo) {
        // 非空校验
        if (StringUtils.isBlank(sql) || pojo == null) {
            return null;
        }
        // 创建 pojolist
        List<T> result = new ArrayList<>();
        // 获得 pojo 所有字段名
        Field[] fields = pojo.getDeclaredFields();
        // 查询数据库得到结果集
        List<HashMap<String, Object>> linkedHashMapsList = genericApiMapper.genericSelect(sql);
        if("java.util.Map".equals(pojo.getTypeName())) {
            return (List<T>) linkedHashMapsList;
        }
        // 如果传进来的 pojo 是基本类型，则走这个分支
        try {
            if ("java.lang.String".equals(pojo.getTypeName())) {
                T pojoObj = pojo.newInstance();
                for (int i = 0; i < linkedHashMapsList.size(); i++) {
                    HashMap<String, Object> stringObjectLinkedHashMap = linkedHashMapsList.get(i);
                    if ((null == stringObjectLinkedHashMap)) {
                        result.add(null);
                        continue;
                    }
                    Set<String> keySet = stringObjectLinkedHashMap.keySet();
                    for (String key : keySet) {
                        String value = stringObjectLinkedHashMap.get(key).toString();
                        pojoObj = (T) value;
                    }
                    result.add(pojoObj);
                }
                return result;
            }
            // 遍历结果集
            for (HashMap<String, Object> linkedHashMap : linkedHashMapsList) {
                // 创建 pojo 对象
                T pojoObj = pojo.newInstance();
                // 将结果集取出来，并转换基本类型,再通过反射 set 到 pojo 中
                Set<String> keySet = linkedHashMap.keySet();
                // 遍历 key
                for (String next : keySet) {
                    for (Field field : fields) {
                        // 当 key 的值等于 pojo 属性名时
                        if (next.equalsIgnoreCase(field.getName())) {
                            // 禁用安全检查以提升性能
                            field.setAccessible(true);
                            // 取出 value，转换成 string 类型
                            String fieldValue = linkedHashMap.get(next).toString();
                            // 获得 pojo 属性类型
                            String type = field.getGenericType().getTypeName();

                            switch (type) {
                                case "java.lang.String": {
                                    // 给 pojo 赋值
                                    field.set(pojoObj, fieldValue);
                                    break;
                                }
                                case "java.util.Date": {
                                    field.set(pojoObj, new SimpleDateFormat("yyyy-MM-dd").parse(fieldValue));
                                    break;
                                }
                                case "java.math.BigDecimal": {
                                    field.set(pojoObj, BigDecimal.valueOf(Double.valueOf(fieldValue)));
                                    break;
                                }
                                case "java.lang.Integer": {
                                    field.set(pojoObj, Integer.valueOf(fieldValue));
                                    break;
                                }
                                case "java.lang.Double": {
                                    field.set(pojoObj, Double.valueOf(fieldValue));
                                    break;
                                }
                                case "java.lang.Boolean": {
                                    field.set(pojoObj, Boolean.valueOf(fieldValue));
                                    break;
                                }
                                case "java.lang.Long": {
                                    field.set(pojoObj, Long.valueOf(fieldValue));
                                    break;
                                }
                                case "java.lang.Float": {
                                    field.set(pojoObj, Float.valueOf(fieldValue));
                                    break;
                                }
                                default:
                                    break;
                            }
                        }
                    }
                }
                // 将赋值完的实体类 add 到 list 中
                result.add(pojoObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
