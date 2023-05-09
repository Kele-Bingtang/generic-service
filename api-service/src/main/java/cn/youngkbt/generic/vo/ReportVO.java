package cn.youngkbt.generic.vo;

import lombok.Data;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 22:26
 * @note
 */
@Data
public class ReportVO {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 报表名称
     */
    private String reportTitle;
    /**
     * 报名描述
     */
    private String description;
    /**
     * 是否允许编辑，0 不允许，1 允许
     */
    private Integer allowEdit;
    /**
     * 是否允许新增，0 不允许，1 允许
     */
    private Integer allowAdd;
    /**
     * 是否允许删除，0 不允许，1 允许
     */
    private Integer allowDelete;
    /**
     * 是否允许查询，0 不允许，1 允许
     */
    private Integer allowFilter;
    /**
     * 是否允许导出，0 不允许，1 允许
     */
    private Integer allowExport;
    /**
     * 报表是否出现行数，0 不允许，1 允许
     */
    private Integer allowRow;
    /**
     * 弹出框宽度
     */
    private String dialogWidth;
    /**
     * 一页显示多少条数据
     */
    private Integer pageSize;
    /**
     * 是否开启图标，0 不开启，1 饼图，2 折线图
     */
    private Integer chartType;
    /**
     * 接口的 id
     */
    private Integer serviceId;
}
