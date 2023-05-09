package cn.youngkbt.generic.common.http.annotation.resolver;

import cn.youngkbt.generic.common.http.annotation.RequestURI;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kele-Bingtang
 * @date 2022-49-22 22:49:50
 * @note 处理 @RequestURI 注解，将请求的地址注入到该注解的参数里
*/
public class RequestUriArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(RequestURI.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();

        // 获取请求地址
        return request.getRequestURI();
    }
}
