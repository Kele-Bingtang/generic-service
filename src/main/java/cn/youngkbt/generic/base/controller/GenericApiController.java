package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.service.GenericApiService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.http.annotation.RequestURI;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 21:24
 * @note
 */
@RestController
@RequestMapping("/generic-api")
@Validated
public class GenericApiController {

    @Resource
    private GenericApiService genericApiService;

    @GetMapping(value = "/{projectUrl}/**")
    public Response queryGenericApi(@PathVariable("projectUrl") String projectUrl, @RequestURI String requestUri,
                                    @NotBlank(message = "无效参数") @RequestHeader("generic_secret_key") String secretKey,
                                    @RequestParam(value = "_from", required = false) String from,
                                    @RequestParam(required = false) Integer pageNo,
                                    @RequestParam(required = false) Integer pageSize) {
        String serviceUrl = this.getServiceUrl(projectUrl, requestUri);
        List<LinkedHashMap<String, Object>> list = genericApiService.queryServiceData("/" + projectUrl, serviceUrl, secretKey, from, pageNo, pageSize);
        return HttpResult.ok(list);
    }

    @PostMapping(value = "/{projectUrl}/**")
    public Response operateGenericApi(@PathVariable("projectUrl") String projectUrl, @RequestURI String requestUri,
                                      @NotBlank(message = "无效参数") @RequestHeader("generic_secret_key") String secretKey,
                                      @RequestParam HashMap<String, Object> data,
                                      HttpServletRequest request) {
        String serviceUrl = this.getServiceUrl(projectUrl, requestUri);
        JSONArray jsonArray = this.getJson(request);
        String result = genericApiService.operateGenericApi("/" + projectUrl, serviceUrl, secretKey, data, jsonArray);
        return HttpResult.ok(result);
    }
    
    public String getServiceUrl(String projectUrl, String requestUri) {
        // 拿出 service 的 uri
        String baseApi = "/generic-api";
        String temp = baseApi + projectUrl;
        // 如 /generic-api/project/service，则 project 是项目地址（projectUrl），service 是接口地址（requestUri）
        return requestUri.substring(temp.length() + 1);
    }

    public JSONArray getJson(HttpServletRequest request) {
        StringBuilder sb = null;
        try {
            // 从前端获取输入字节流
            ServletInputStream requestInputStream = request.getInputStream();
            // 将字节流转换为字符流,并设置字符编码为 utf-8
            InputStreamReader ir = new InputStreamReader(requestInputStream,"utf-8");
            // 使用字符缓冲流进行读取
            BufferedReader br = new BufferedReader(ir);
            // 开始拼装 JSON 字符串
            String line = null;
            sb = new StringBuilder();
            while((line = br.readLine())!=null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return JSON.parseArray(sb.toString());
    }

}
