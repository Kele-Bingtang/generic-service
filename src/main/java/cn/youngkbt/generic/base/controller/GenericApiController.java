package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.service.GenericApiService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.http.annotation.RequestURI;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

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

    @GetMapping(value = "/{projectUrl}/**")
    public Response queryGenericApi(@PathVariable("projectUrl") String projectUrl, @RequestURI String requestUri, @RequestHeader("Authorization") String secretKey) {
        Assert.notNull(secretKey, "无效参数");
        // 拿出 service 的 uri
        String baseApi = "/generic-api";
        String temp = baseApi + projectUrl;
        String serviceUrl = requestUri.substring(temp.length() + 1);
        List<HashMap<String, Object>> list = genericApiService.queryServiceData("/" + projectUrl, serviceUrl, secretKey);
        return HttpResult.ok(list);
    }
    
}
