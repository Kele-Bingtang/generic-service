package cn.youngkbt.generic.common.valid.annotation;

import cn.youngkbt.generic.common.valid.ValidFieldsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Kele-Bingtang
 * @date 2023/5/3 16:51
 * @note 指定多个属性不为空，可设置全满足或任意满足即可通过验证
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Constraint(validatedBy = {ValidFieldsValidator.class})
public @interface ValidFields {
    String[] fields();

    boolean any() default false;

    String message() default "Invalid attribute";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
