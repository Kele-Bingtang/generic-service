package cn.youngkbt.generic.common.http;

import java.io.Serializable;

/**
 * @author kele-Bingtang
 * @date 2022/4/30 14:47
 * @note 响应对象
 */
public class Response<T> implements Serializable {
    private static final long serialVersionUID = -464624820023286858L;
    /** 自定义状态码 **/
    private Integer code;
    /** 状态码信息 **/
    protected String status;
    /** 消息 **/
    private String message;
    /** 数据 **/
    protected transient T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
