package cn.youngkbt.valid.annotation;

import cn.youngkbt.valid.IncludeValidatorClass;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Kele-Bingtang
 * @date 2022/12/6 22:17
 * @note 指定 value，如果 Controller 接受的参数不包含 value，则校验失败
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = IncludeValidatorClass.class)
public @interface IncludeValid {

    String[] value() default {};

    String message() default "data is not available";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
