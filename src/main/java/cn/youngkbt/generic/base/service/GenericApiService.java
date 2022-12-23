package cn.youngkbt.generic.base.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 22:58
 * @note
 */
public interface GenericApiService {
    
    public List<HashMap<String, Object>> queryServiceData(String projectUrl, String serviceUrl, String secretKey);
    
    public List<HashMap<String, Object>> genericSelect(String sql);
    
}
