package cn.youngkbt.generic.common.http;

/**
 * @author Young Kbt
 * @date 2022/4/30 15:19
 * @note 响应状态枚举类
 */
public enum ResponseStatusEnum {
    SUCCESS(200, "success", "操作成功！"),
    FAIL(600, "fail", "操作失败！"),
    ERROR(700, "error", "操作错误！"),
    NO_PERMISSION(800, "fail", "没有权限"),
    VALIDATION_ERROR(1001, "error", "传递的参数不符合要求"),
    CONDITION_SQL_ERROR(1002, "error", "字段不存在"),
    LOGIN_FAIL(1003, "fail", "登录失败"),
    USER_REGISTER_FAILED(1004, "fail", "注册失败"),
    USER_ACCOUNT_EXISTED(1005, "fail", "用户名已存在"),
    USER_ACCOUNT_EXPIRED(1006, "fail", "账号过期"),
    USERNAME_PASSWORD_ERROR(1007, "fail", "用户名或密码错误"),
    USER_PASSWORD_EXPIRED(1008, "fail", "账号过期"),
    USER_ACCOUNT_DISABLE(1009, "fail", "账号禁用"),
    USER_ACCOUNT_LOCKED(1010, "fail", "账号锁定"),
    USER_ACCOUNT_NOT_EXIST(1011, "fail", "账号不存在"),
    SERVICE_SQL_EXCEPTION(1012, "error", "SQL 无法执行，请检查 SQL 语法、表是否存在、表名前是否加上数据库名"),
    NO_EXIST_WHERE_KEY(1013, "error", "主键不存在"),
    NO_EXIST_WHERE_DATA(1013, "error", "数据不存在"),
    NO_EXIST_TABLE(1013, "error", "表名不存在"),
    QUERY_FILTER_FIAL(1014, "fail", "查询失败，条件不存在"),
    PROJECT_EXEIT(1015, "fail", "项目已存在"),
    CATEGORY_EXEIT(1016, "fail", "目录已存在"),
    SERVICE_EXEIT(1016, "fail", "接口已存在"),
    SERVICE_COL_EXEIT(1017, "fail", "字段已存在"),
    FIELD_EXEIT(1018, "fail", "属性已存在"),
    PARAMS_ERROR(1019, "fail", "参数出错"),
    DROPDOWN_ERROR(1020, "fail", "下拉值处理异常");

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
