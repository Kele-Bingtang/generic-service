package cn.youngkbt.generic.common.http.annotation;

import java.lang.annotation.*;

/**
 * @author Kele-Bingtang
 * @date 2023/1/15 19:36
 * @note
 */

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestData {

    String value() default "";

    /**
     * 参数是否是必填的，默认 false，可以设置为 true
     */
    boolean required() default false;

}
