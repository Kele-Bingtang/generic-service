package cn.youngkbt.generic.common.valid;


import cn.youngkbt.generic.common.valid.annotation.IncludeValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Kele-Bingtang
 * @date 2022/12/6 22:17
 * @note 如果 Controller 接受的参数不包含 value，则校验失败
 */
public class IncludeValidValidator implements ConstraintValidator<IncludeValid, Integer> {
    private String[] values;

    @Override
    public void initialize(IncludeValid constraintAnnotation) {
        this.values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;
        if (null == value) {
            return true;
        }
        for (String s : values) {
            if (s.equals(String.valueOf(value))) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }
}
