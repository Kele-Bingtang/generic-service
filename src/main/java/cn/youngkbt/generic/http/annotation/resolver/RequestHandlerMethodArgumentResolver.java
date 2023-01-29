package cn.youngkbt.generic.http.annotation.resolver;

import cn.youngkbt.generic.http.annotation.RequestData;
import cn.youngkbt.generic.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2023/1/15 19:35
 * @note
 */
public class RequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * 解析 Content-Type 为 application/json 的默认解析器是 RequestResponseBodyMethodProcessor
     */
    private RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;

    /**
     * 解析 Content-Type 为 application/x-www-form-urlencoded 的默认解析器是 ServletModelAttributeMethodProcessor
     */
    private ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor;

    public RequestHandlerMethodArgumentResolver(RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor,
                                                ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor) {
        this.requestResponseBodyMethodProcessor = requestResponseBodyMethodProcessor;
        this.servletModelAttributeMethodProcessor = servletModelAttributeMethodProcessor;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasMethodAnnotation(RequestData.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        final String applicationXwwwFormUrlencoded = MediaType.APPLICATION_FORM_URLENCODED_VALUE;
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            throw new RuntimeException(" request must not be null!");
        }
        String contentType = request.getContentType();
        // 获取 uri 上的参数
        Map<String, String[]> params = request.getParameterMap();
        Map<String, Object> paramMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(params)) {
            for (Map.Entry<String, String[]> entry : params.entrySet()) {
                String key = entry.getKey();
                String[] value = entry.getValue();
                if (value != null && value.length == 1) {
                    paramMap.put(key, value[0]);
                }
            }
        }
        // 如果入参是拼接在 uri 上，则直接把拼在 uri 的参数封装成一个对象
        if (StringUtils.isBlank(contentType) && !CollectionUtils.isEmpty(paramMap)) {
            Class classType = methodParameter.getParameterType();
            Object model = Map.class.isAssignableFrom(classType) ? paramMap : JSONObject.parseObject(JSONObject.toJSONString(paramMap), classType);
            return model;
        }
        /*
         * 如果 ContentType 是 application/x-www-form-urlencoded，那么使用 ServletModelAttributeMethodProcessor 解析器
         *
         * 注:其实默认的，当系统识别到参数前有 @RequestBody 注解时，就会走 RequestResponseBodyMethodProcessor 解析器;这里就
         * 相当于在走默认的解析器前走了个判断而已。
         */
        if (applicationXwwwFormUrlencoded.equals(contentType)) {
            return servletModelAttributeMethodProcessor.resolveArgument(methodParameter,
                    modelAndViewContainer, nativeWebRequest, webDataBinderFactory);
        }
        return requestResponseBodyMethodProcessor.resolveArgument(methodParameter,
                modelAndViewContainer, nativeWebRequest, webDataBinderFactory);
    }
}