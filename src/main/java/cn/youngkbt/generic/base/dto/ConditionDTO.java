package cn.youngkbt.generic.base.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Kele-Bingtang
 * @date 2022/12/5 23:02
 * @note
 */
public class ConditionDTO implements Serializable {

    private static final long serialVersionUID = -5099378457111419832L;
    /**
     * 数据库字段名
     */
    @NotBlank(message = "字段名不能为空")
    private String column;
    /**
     * 字段值
     */
    private String value;
    /**
     * 连接类型，如 like，equals，gt，ge，lt，le，具体看 cn.youngkbt/vo/ConditionVo.java
     */
    private String type = "like";

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
