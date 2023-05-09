package cn.youngkbt.generic.common.dto.report;

import cn.youngkbt.generic.common.valid.annotation.IncludeValid;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 22:17
 * @note
 */
@Data
public class ReportUpdateDTO {
    /**
     * 主键
     */
    @Null(message = "修改时 id 必须为空")
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
    @IncludeValid(value = {"0", "1"}, message = "allowEdit 只有 0 不允许，1 允许")
    private Integer allowEdit;
    /**
     * 是否允许新增，0 不允许，1 允许
     */
    @IncludeValid(value = {"0", "1"}, message = "allowAdd 只有 0 不允许，1 允许")
    private Integer allowAdd;
    /**
     * 是否允许删除，0 不允许，1 允许
     */
    @IncludeValid(value = {"0", "1"}, message = "allowDelete 只有 0 不允许，1 允许")
    private Integer allowDelete;
    /**
     * 是否允许查询，0 不允许，1 允许
     */
    @IncludeValid(value = {"0", "1"}, message = "allowFilter 只有 0 不允许，1 允许")
    private Integer allowFilter;
    /**
     * 是否允许导出，0 不允许，1 允许
     */
    @IncludeValid(value = {"0", "1"}, message = "allowExport 只有 0 不允许，1 允许")
    private Integer allowExport;
    /**
     * 报表是否出现行数，0 不允许，1 允许
     */
    @IncludeValid(value = {"0", "1"}, message = "报表是否出现行数，0 不允许，1 允许")
    private Integer allowRow;
    /**
     * 弹出框宽度
     */
    private String dialogWidth;
    /**
     * 一页显示多少条数据
     */
    @Min(value = 1, message = "不能低于 1 条")
    @Max(value = 999, message = "不能高于 999 条")
    private Integer pageSize;
    /**
     * 是否开启图标，0 不开启，1 饼图，2 折线图
     */
    @IncludeValid(value = {"0", "1", "2"}, message = "目前仅支持 0 不开启，1 饼图，2 折线图")
    private Integer chartType;
    /**
     * 修改报表设置的用户 id
     */
    @NotNull(message = "修改的用户名不能为空")
    private String modifyUser;

}
