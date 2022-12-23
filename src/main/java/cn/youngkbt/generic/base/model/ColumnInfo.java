package cn.youngkbt.generic.base.model;

import lombok.Data;

/**
 * @author Kele-Bingtang
 * @date 2022/12/23 22:14
 * @note 字段信息类
 */
@Data
public class ColumnInfo {
    /**
     * 列名
     */
    private String columnName;
    /**
     * 数据类型
     */
    private String columnType;
    /**
     * 字段类型
     */
    private String dataType;
    /**
     * 类型长度
     */
    private String length;
    /**
     * 是否为空
     */
    private String isNull;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 备注
     */
    private String comment;
}
