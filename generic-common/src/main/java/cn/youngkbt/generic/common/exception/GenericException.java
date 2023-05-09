package cn.youngkbt.generic.common.exception;


import cn.youngkbt.generic.common.http.ResponseStatusEnum;

/**
 * @author Kele-Bingtang
 * @date 2022/12/7 22:41
 * @note Generic 异常类
 */
public class GenericException extends RuntimeException {
    private Integer code;
    private String status;
    private String message;

    public GenericException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public GenericException(Integer code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public GenericException(ResponseStatusEnum responseStatusEnum) {
        this.code = responseStatusEnum.getCode();
        this.status = responseStatusEnum.getStatus();
        this.message = responseStatusEnum.getMessage();
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
