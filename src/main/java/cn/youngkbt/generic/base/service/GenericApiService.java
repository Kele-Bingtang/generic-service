package cn.youngkbt.generic.base.service;

import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 22:58
 * @note
 */
public interface GenericApiService {
    
    public List<LinkedHashMap<String, Object>> queryServiceData(String projectUrl, String serviceUrl, String secretKey, String from, Integer pageNo, Integer pageSize);
    
    public String operateGenericApi(String projectUrl, String serviceUrl, String secretKey, HashMap<String, Object> data, JSONArray jsonArray);
}
