package cn.youngkbt.generic.base.service;

import com.alibaba.fastjson.JSONArray;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 22:58
 * @note
 */
public interface ApiService {
    
    public List<LinkedHashMap<String, Object>> queryServiceData(String projectUrl, String serviceUrl, String secretKey, Map<String, Object> request);
    
    public String operateGenericApi(String projectUrl, String serviceUrl, String secretKey, Map<String, Object> data, JSONArray jsonArray);
    
}
