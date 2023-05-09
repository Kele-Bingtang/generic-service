package cn.youngkbt.generic.common.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 23:31
 * @note 从 Spring 容器拿到 Bean
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBeanById(String id) {
        return applicationContext == null ? null : applicationContext.getBean(id);
    }

    public static <T> T getBeanByClass(Class<T> c) {
        return applicationContext == null ? null : applicationContext.getBean(c);
    }

    public static Map getBeansByClass(Class c) {
        return applicationContext == null ? null : applicationContext.getBeansOfType(c);
    }

}