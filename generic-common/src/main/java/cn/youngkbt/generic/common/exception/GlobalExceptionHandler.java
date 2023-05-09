package cn.youngkbt.generic.common.exception;

import cn.youngkbt.generic.common.http.HttpResult;
import cn.youngkbt.generic.common.http.Response;
import cn.youngkbt.generic.common.http.ResponseStatusEnum;
import cn.youngkbt.generic.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;


/**
 * @author Kele-Bingtang
 * @date 2022/12/6 21:27
 * @note 全局异常捕获
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * 数据校验全局处理
     * BindException是 @Valid 使用校验失败时产生的异常
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Response bindExceptionHandler(BindException e) {
        // 获取实体类定义的校验注解字段上的 message 作为异常信息，@NotBlank(message = "用户密码不能为空！")，异常信息即为"用户密码不能为空！
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        LOGGER.error("BindException：{}", defaultMessage);
        return HttpResult.response(null, ResponseStatusEnum.VALIDATION_ERROR.getCode(), ResponseStatusEnum.VALIDATION_ERROR.getStatus(), defaultMessage);
    }

    /**
     * Hibernate validator 参数校验
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Response validatorHandler(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        assert fieldError != null;
        String defaultMessage = fieldError.getDefaultMessage();
        LOGGER.error("MethodArgumentNotValidException：{}", defaultMessage);
        return HttpResult.response(null, ResponseStatusEnum.VALIDATION_ERROR.getCode(), ResponseStatusEnum.VALIDATION_ERROR.getStatus(), defaultMessage);
    }

    /**
     * Hibernate validator参数校验
     */
    @ExceptionHandler(ValidationException.class)
    public Response handleValidationException(ValidationException e) {
        String defaultMessage = e.getCause().getMessage();
        LOGGER.error("ValidationException：{}", defaultMessage);
        return HttpResult.response(null, ResponseStatusEnum.VALIDATION_ERROR.getCode(), ResponseStatusEnum.VALIDATION_ERROR.getStatus(), defaultMessage);
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Response handleConstraintViolationException(ConstraintViolationException e) {
        LOGGER.error("ConstraintViolationException：{}", e.getMessage());
        return HttpResult.response(null, ResponseStatusEnum.VALIDATION_ERROR.getCode(), ResponseStatusEnum.VALIDATION_ERROR.getStatus(), e.getMessage());
    }

    /**
     * 自定义异常：ConditionVoSqlException
     */
    @ExceptionHandler(GenericException.class)
    public Response handleConditionSqlException(GenericException e) {
        LOGGER.error("ConstraintViolationException：{}", e.getMessage());
        if (StringUtils.isNotBlank(e.getStatus())) {
            return HttpResult.response(null, e.getCode(), e.getStatus(), e.getMessage());
        }
        return HttpResult.response(null, e.getCode(), "error", e.getMessage());
    }

    /**
     * 全局异常处理
     * 如果设置了特定异常处理，全局异常处理可作为兜底异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response exceptionHandler(Exception e) {
        LOGGER.error("Exception：{}", e.getMessage());
        return HttpResult.error(null);
    }
}
