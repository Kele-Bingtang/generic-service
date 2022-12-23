package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.GenericApiMapper;
import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.service.GenericApiService;
import cn.youngkbt.generic.base.service.GenericProjectService;
import cn.youngkbt.generic.base.service.GenericServiceService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

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
    private GenericApiMapper genericApiMapper;

    @Override
    public List<HashMap<String, Object>> queryServiceData(String projectUrl, String serviceUrl, String secretKey) {
        GenericProject project = genericProjectService.queryGenericOneProjectBySecretKey(secretKey);
        Assert.notNull(project, "项目不存在");
        Assert.isTrue(projectUrl.equals(project.getBaseUrl()), "违法的 baseUrl");
        GenericService genericService = new GenericService();
        genericService.setProjectId(project.getId());
        genericService.setServiceUrl(serviceUrl);
        List<GenericService> serviceList = genericServiceService.queryGenericServiceList(genericService);
        Assert.isTrue(serviceList.size() == 1, "该接口的 api 和其他接口重复，请修改该接口的 api");
        GenericService service = serviceList.get(0);
        List<HashMap<String, Object>> list = this.genericSelect(service.getSelectSql());
        return list;
    }

    public List<HashMap<String, Object>> genericSelect(String sql) {
        return genericApiMapper.genericSelect(sql);
    }

}
