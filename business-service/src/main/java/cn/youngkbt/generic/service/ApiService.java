package cn.youngkbt.generic.service;

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

    /**
     * 处理 API 的 GET 请求
     * @param projectUrl 项目 url
     * @param serviceUrl 接口 url
     * @param secretKey 项目密钥
     * @param request 请求对象
     * @return 数据
     */
    public List<LinkedHashMap<String, Object>> queryServiceData(String projectUrl, String serviceUrl, String secretKey, Map<String, Object> request);

    /**
     * 处理 API 的 POST 请求
     * @param projectUrl 项目 url
     * @param serviceUrl 接口 url
     * @param secretKey 项目密钥
     * @param data 执行增上哎的数据
     * @param jsonArray JSON 数据
     * @return 数据
     */
    public String operateGenericApi(String projectUrl, String serviceUrl, String secretKey, Map<String, Object> data, JSONArray jsonArray);
    
}
