package cn.youngkbt.config;

import cn.youngkbt.intercept.LogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor());
        // 可以具体制定哪些需要拦截，哪些不拦截，其实也可以使用自定义注解更灵活完成
        //        .addPathPatterns("/**")
        //       .excludePathPatterns("/test.html");
    }
}
