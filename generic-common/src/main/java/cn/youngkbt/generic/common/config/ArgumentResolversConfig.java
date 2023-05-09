package cn.youngkbt.generic.common.config;

import cn.youngkbt.generic.common.http.annotation.resolver.RequestHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2023/1/15 20:23
 * @note 自定义注解相关配置
 */
@Configuration
public class ArgumentResolversConfig {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    public ArgumentResolversConfig(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
    }

    @PostConstruct
    private void addArgumentResolvers() {
        // 获取到的是不可变的集合
        List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
        RequestHandlerMethodArgumentResolver requestHandlerMethodArgumentResolver = getRequestHandlerMethodArgumentResolver(argumentResolvers);
        // 获取到的是不可变的集合,所以我们需要新建一个集合来放置参数解析器
        List<HandlerMethodArgumentResolver> myArgumentResolvers = new ArrayList<>(argumentResolvers.size() + 1);
        // 将自定义的解析器，放置在第一个；并保留原来的解析器
        myArgumentResolvers.add(requestHandlerMethodArgumentResolver);
        myArgumentResolvers.addAll(argumentResolvers);
        requestMappingHandlerAdapter.setArgumentResolvers(myArgumentResolvers);
    }

    /**
     * 获取 MyHandlerMethodArgumentResolver 实例
     */
    private RequestHandlerMethodArgumentResolver getRequestHandlerMethodArgumentResolver(
            List<HandlerMethodArgumentResolver> argumentResolversList) {
        // 解析 Content-Type 为 application/json 的默认解析器
        RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor = null;
        // 解析 Content-Type为 application/x-www-form-urlencoded 的默认解析器
        ServletModelAttributeMethodProcessor servletModelAttributeMethodProcessor = null;

        for (HandlerMethodArgumentResolver argumentResolver : argumentResolversList) {
            if (requestResponseBodyMethodProcessor != null && servletModelAttributeMethodProcessor != null) {
                break;
            }
            if (argumentResolver instanceof RequestResponseBodyMethodProcessor) {
                requestResponseBodyMethodProcessor = (RequestResponseBodyMethodProcessor) argumentResolver;
                continue;
            }
            if (argumentResolver instanceof ServletModelAttributeMethodProcessor) {
                servletModelAttributeMethodProcessor = (ServletModelAttributeMethodProcessor) argumentResolver;
            }
        }
        if (requestResponseBodyMethodProcessor == null || servletModelAttributeMethodProcessor == null) {
            throw new RuntimeException("requestResponseBodyMethodProcessor and "
                    + " servletModelAttributeMethodProcessor must not be null!");
        }
        return new RequestHandlerMethodArgumentResolver(requestResponseBodyMethodProcessor,
                servletModelAttributeMethodProcessor);
    }
}