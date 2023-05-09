package cn.youngkbt.generic.controller;

import cn.youngkbt.generic.common.http.HttpResult;
import cn.youngkbt.generic.common.http.Response;
import cn.youngkbt.generic.common.http.annotation.RequestURI;
import cn.youngkbt.generic.common.utils.StringUtils;
import cn.youngkbt.generic.service.ApiService;
import cn.youngkbt.generic.service.UserService;
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
import java.util.*;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 21:24
 * @note Generic API 接口
 */
@RestController
@RequestMapping("/generic-api")
@Validated
public class ApiController {

    @Resource
    private ApiService apiService;
    @Resource
    private UserService userService;

    /**
     * API GET 请求
     * @param projectUrl 项目地址
     * @param requestUri 请求的 URI，不包含域名
     * @param secretKey 项目密钥
     * @param token 用户登录 token
     * @param request 请求对象
     * @return 查询结果
     */
    @GetMapping(value = "/{projectUrl}/**")
    public Response<Object> queryGenericApi(@PathVariable("projectUrl") String projectUrl, @RequestURI String requestUri,
                                            @RequestHeader(value = "Secret-Key", required = false) String secretKey,
                                            @RequestHeader(value = "Authorization", required = false) String token,
                                            HttpServletRequest request) {
        Map<String, Object> requestParamsMap = this.getParameterMap(request);
        String secretKeyParam = (String) requestParamsMap.get("secretKey");
        if (StringUtils.isBlank(secretKey, secretKeyParam)) {
            return HttpResult.fail("您没有权限访问");
        } else if (StringUtils.isNotBlank(secretKeyParam)) {
            secretKey = secretKeyParam;
        }
        String serviceUrl = this.getServiceUrl(projectUrl, requestUri);
        // 验证是否对项目有权限
        userService.queryUserRoleByToken(secretKey, serviceUrl, token);
        List<LinkedHashMap<String, Object>> list = apiService.queryServiceData("/" + projectUrl, serviceUrl, secretKey, requestParamsMap);
        return HttpResult.ok(list);
    }

    @PostMapping(value = "/{projectUrl}/**")
    public Response<String> operateGenericApi(@PathVariable("projectUrl") String projectUrl, @RequestURI String requestUri,
                                              @NotBlank(message = "无效参数") @RequestHeader("Secret-Key") String secretKey,
                                              @RequestHeader(value = "Authorization", required = false) String token,
                                              HttpServletRequest request) {
        Map<String, Object> requestParamsMap = this.getParameterMap(request);
        String secretKeyParam = (String) requestParamsMap.get("secretKey");
        if (StringUtils.isBlank(secretKey, secretKeyParam)) {
            return HttpResult.fail("您没有权限访问");
        } else if (StringUtils.isNotBlank(secretKeyParam)) {
            secretKey = secretKeyParam;
        }
        String serviceUrl = this.getServiceUrl(projectUrl, requestUri);
        userService.queryUserRoleByToken(secretKey, serviceUrl, token);
        // 支持 JSON
        JSONArray jsonArray = this.getJson(request);
        String result = apiService.operateGenericApi("/" + projectUrl, serviceUrl, secretKey, requestParamsMap, jsonArray);
        return HttpResult.okMessage(result);
    }

    /**
     * 获取接口的 URL 地址
     * @param projectUrl 项目地址
     * @param requestUri 请求的 URI
     * @return 接口的 URL 地址
     */
    public String getServiceUrl(String projectUrl, String requestUri) {
        // 拿出 service 的 uri
        String baseApi = "/generic-api";
        String temp = baseApi + projectUrl;
        // 如 /generic-api/project/service，则 project 是项目地址（projectUrl），service 是接口地址（requestUri）
        return requestUri.substring(temp.length() + 1);
    }

    /**
     * 获取 JSON 数据
     * @param request 请求对象
     * @return JSON 数据
     */
    public JSONArray getJson(HttpServletRequest request) {
        StringBuilder sb = null;
        try {
            // 从前端获取输入字节流
            ServletInputStream requestInputStream = request.getInputStream();
            // 将字节流转换为字符流,并设置字符编码为 utf-8
            InputStreamReader ir = new InputStreamReader(requestInputStream, "utf-8");
            // 使用字符缓冲流进行读取
            BufferedReader br = new BufferedReader(ir);
            // 开始拼装 JSON 字符串
            String line;
            sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return JSON.parseArray(sb.toString());
    }

    /**
     * 从请求里读取参数，转成 Map
     * @param request 请求对象
     * @return 参数 Map
     */
    private Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>(16);
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            } else if (paramValues.length > 0) {
                // 数组
                map.put(paramName, paramValues);
            }
        }
        return map;
    }
}
