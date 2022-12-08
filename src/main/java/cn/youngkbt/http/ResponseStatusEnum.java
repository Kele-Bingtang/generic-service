package cn.youngkbt.http;

/**
 * @author Young Kbt
 * @date 2022/4/30 15:19
 * @note 响应状态枚举类
 */
public enum ResponseStatusEnum {
    SUCCESS(200, "success", "操作成功！"),
    FAIL(600, "fail", "操作失败！"),
    ERROR(700, "error", "操作错误！"),
    VALIDATIONEXCEPTION(800, "error", "传递的参数不符合要求"),
    CONDITIONSQLEXCEPTION(801, "error", "字段不存在");

    private Integer code;

    private String status;

    private String message;

    private ResponseStatusEnum(Integer code, String status, String message) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
