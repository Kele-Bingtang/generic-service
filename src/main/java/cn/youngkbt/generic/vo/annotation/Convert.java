package cn.youngkbt.generic.vo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.*;

/**
 * @author Kele-Bingtang
 * @date 2022/12/18 17:20
 * @note
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD} )
public @interface Convert {
    int[] source() default {};
    
    String[] target() default {};
}
