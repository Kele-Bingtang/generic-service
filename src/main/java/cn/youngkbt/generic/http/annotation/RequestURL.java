package cn.youngkbt.generic.http.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.*;

/**
 * @author Kele-Bingtang
 * @date 2022/12/22 22:40
 * @note 读取前端传来的 url，注入指定的参数里
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestURL {
}
