package cn.youngkbt.generic.config;

import cn.youngkbt.generic.http.annotation.resolver.RequestUriArgumentResolver;
import cn.youngkbt.generic.http.annotation.resolver.RequestUrlArgumentResolver;
import cn.youngkbt.generic.log.LogInterceptor;
import cn.youngkbt.generic.security.SecurityInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022/12/8 23:22
 * @note
 */
@Configuration
public class WebConfigurerAdapter implements WebMvcConfigurer {

    /**
     * 解决跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false)
                .allowedOriginPatterns("*")
                .maxAge(3600);
    }

    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Bean
    public SecurityInterceptor securityInterceptor() {
        return new SecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor());
        registry.addInterceptor(securityInterceptor())
                .addPathPatterns("/project/**", "/category/**", "/service/**", "/field/**", "/report/**", "/serviceCol/**",
                        "/user/queryMemberInProject/**", "/user/queryAllMemberNotInProject/**", "/user/updateUserRole", "/user/insertUserProject/**", "/user/removeOneMember")
                .excludePathPatterns("/project/query*/**", "/generic-api/**");
    }

    /**
     * 添加自定义注解
     *
     * @param resolvers initially an empty list
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestUrlArgumentResolver());
        resolvers.add(new RequestUriArgumentResolver());
    }
}
