package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.service.GenericApiService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.http.annotation.RequestURI;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 21:24
 * @note 
 */
@RestController
@RequestMapping("/generic-api")
public class GenericApiController {
    
    @Resource
    private GenericApiService genericApiService;
    
    @GetMapping("/{projectUrl}/**")
    public Response queryGenericApi(@PathVariable("projectUrl") String projectUrl, @RequestURI String requestUri) {
        // 拿出 service 的 uri
        String baseApi = "/generic-api";
        String temp = baseApi + projectUrl;
        String serviceUrl = requestUri.substring(temp.length() + 1);
        List<Map> list = genericApiService.queryServiceData("/" + projectUrl, serviceUrl, "c012186ede8b4532a778682376d2cb59");
        return HttpResult.ok(list);
    }
}
