package cn.youngkbt.generic.base.service;

import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 22:58
 * @note
 */
public interface GenericApiService {
    
    public List<Map> queryServiceData(String projectUrl, String serviceUrl, String secretKey);
}
