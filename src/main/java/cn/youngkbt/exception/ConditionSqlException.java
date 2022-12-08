package cn.youngkbt.exception;

import cn.youngkbt.http.ResponseStatusEnum;

/**
 * @author Kele-Bingtang
 * @date 2022/12/7 22:41
 * @note
 */
public class ConditionSqlException extends RuntimeException {
    private Integer code;
    private String message;

    public ConditionSqlException() {
        this.code = ResponseStatusEnum.CONDITIONSQLEXCEPTION.getCode();
        this.message = ResponseStatusEnum.CONDITIONSQLEXCEPTION.getMessage();
    }

    public ConditionSqlException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
