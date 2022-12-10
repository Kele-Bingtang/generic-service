package cn.youngkbt.generic.http;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kele-Bingtang
 * @date 2022/4/30 14:47
 * @note 响应对象
 */
public class Response implements Serializable {
    private static final long serialVersionUID = -464624820023286858L;
    /** 自定义状态码 **/
    private Integer code;
    /** 状态码信息 **/
    protected String codeMessage;
    /** 消息 **/
    private String message;
    /** 数据 **/
    protected transient Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCodeMessage() {
        return codeMessage;
    }

    public void setCodeMessage(String codeMessage) {
        this.codeMessage = codeMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
