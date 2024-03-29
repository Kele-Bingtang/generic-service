package cn.youngkbt.generic.vo;

import lombok.Data;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 23:08
 * @note
 */
@Data
public class ServiceColVO {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 表字段名称
     */
    private String tableCol;
    /**
     * 请求返回的 JSON 字段名称
     */
    private String jsonCol;
    /**
     * 报表字段名称
     */
    private String reportCol;
    /**
     * 字段类型
     */
    private String colType;
    /**
     * 字段类型长度
     */
    private Integer colLength;
    /**
     * 增删改时，是否作为 where 条件，0 不作为，1 作为（值为空传）2 作为（值为空不传）
     */
    private Integer isWhereKey;
    /**
     * 字段默认值
     */
    private String defaultValue;
    /**
     * 数据是否加密，0 不加密，1 加密
     */
    private Integer dataEncrypt;
    /**
     * select 时的条件筛选，如 where 字段 = xx、like %xx%。
     * 0 为不筛选
     * 1 为 =，精准匹配
     * 2 为 !=，不等于
     * 3 为 like %xx
     * 4 为 like xx%
     * 5 为 like %xx%
     * 6 为 lt，即小于
     * 7 为 gt，即大于
     * 8 为 le，即小于等于
     * 9 为 ge，即大于等于
     * 10 为 value range，即值范围查询
     */
    private Integer queryFilter;
    /**
     * 排序，负数表示 desc，正数表示 asc
     */
    private Integer orderBy;
    /**
     * 是否允许插入，0 不允许，1 允许
     */
    private Integer allowInsert;
    /**
     * 是否允许更新，0 不允许，1 允许
     */
    private Integer allowUpdate;
    /**
     * 是否允许查询，0 不允许，1 允许
     */
    private Integer allowFilter;
    /**
     * 是否允许返回在请求里，0 不允许，1 允许
     */
    private Integer allowRequest;
    /**
     * 是否允许出现在报表，0 不允许，1 允许
     */
    private Integer allowShowInReport;
    /**
     * 是否允许出现在报表的增删改弹出框，0 不允许，1 允许
     */
    private Integer allowShowInDetail;
    /**
     * 报表字段出现的顺序
     */
    private Integer displaySeq;
    /**
     * 报表字段显示的宽度，-1 为 auto，其他为准确的数值 + px
     */
    private Integer reportColWidth;
    /**
     * 报表的增删改弹出框，该字段的输入框宽度，-1 为 auto，其他为准确的数值+px
     */
    private Integer detailColWidth;
    /**
     * 列对齐，0 为左对齐，1 为居中，2 为右对齐
     */
    private Integer colAlign;
    /**
     * 自定义下拉值
     */
    private String dropdownValue;
    /**
     * 读取接口获取下拉值
     */
    private String dropdownService;
    /**
     * 通过 sql 获取下拉值
     */
    private String dropdownSql;
}
