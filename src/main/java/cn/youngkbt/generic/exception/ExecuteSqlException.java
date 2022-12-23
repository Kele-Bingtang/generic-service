package cn.youngkbt.generic.exception;

import cn.youngkbt.generic.http.ResponseStatusEnum;

/**
 * @author Kele-Bingtang
 * @date 2022/12/7 22:41
 * @note
 */
public class ExecuteSqlException extends RuntimeException {
    private Integer code;
    private String status;
    private String message;

    public ExecuteSqlException() {
        this.code = ResponseStatusEnum.CONDITION_SQL_ERROR.getCode();
        this.message = ResponseStatusEnum.CONDITION_SQL_ERROR.getMessage();
    }

    public ExecuteSqlException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public ExecuteSqlException(Integer code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
