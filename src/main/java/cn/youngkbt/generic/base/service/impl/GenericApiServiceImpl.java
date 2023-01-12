package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.GenericApiMapper;
import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.model.ServiceCol;
import cn.youngkbt.generic.base.service.GenericApiService;
import cn.youngkbt.generic.base.service.GenericProjectService;
import cn.youngkbt.generic.base.service.GenericServiceService;
import cn.youngkbt.generic.base.service.ServiceColService;
import cn.youngkbt.generic.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<HashMap<String, Object>> queryServiceData(String projectUrl, String serviceUrl, String secretKey, String from) {
        GenericProject project = genericProjectService.queryGenericOneProjectBySecretKey(secretKey);
        Assert.notNull(project, "项目不存在");
        Assert.isTrue(projectUrl.equals(project.getBaseUrl()), "违法的 baseUrl");
        GenericService genericService = new GenericService();
        genericService.setProjectId(project.getId());
        genericService.setServiceUrl(serviceUrl);
        GenericService service = genericServiceService.queryOneGenericService(genericService);
        // 通过 selectSql 拿到数据
        List<HashMap<String, Object>> list = this.genericSelect(service.getSelectSql());
        // 获取数据对应的字段配置信息
        ServiceCol serviceCol = new ServiceCol();
        serviceCol.setServiceId(service.getId());
        List<ServiceCol> serviceColList = serviceColService.queryServiceColList(serviceCol);
        if(StringUtils.isNotBlank(from) && from.equals("report")) {
            
        }
        return this.processMapping(serviceColList, list);
    }

    public List<HashMap<String, Object>> genericSelect(String sql) {
        return genericApiMapper.genericSelect(sql);
    }

    public List<HashMap<String, Object>> processMapping(List<ServiceCol> serviceColList, List<HashMap<String, Object>> list) {
        List<HashMap<String, Object>> tempList = new ArrayList<>();
        list.forEach(map -> {
            HashMap<String, Object> tempMap = new HashMap<>(serviceColList.size());
            for (ServiceCol serviceCol : serviceColList) {
                boolean isMatch = false;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (serviceCol.getTableCol().equals(entry.getKey())) {
                        isMatch = true;
                        tempMap.put(serviceCol.getJsonCol(), entry.getValue());
                        break;
                    } else {
                        isMatch = false;
                    }
                }
                // serviceColList 存在的，list 里不存在，但是也要添加进去 tempList
                if (!isMatch) {
                    tempMap.put(serviceCol.getJsonCol(), "");
                }
            }
            tempList.add(tempMap);
        });
        return tempList;
    }
   
}
