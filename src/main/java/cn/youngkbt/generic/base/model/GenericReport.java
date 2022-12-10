package cn.youngkbt.generic.base.model;

import cn.youngkbt.generic.valid.annotation.IncludeValid;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.sql.Timestamp;

/**
 * @author Kele-Bingtang
 * @version 1.0
 * @since 2022-12-03 22:45:22
 */
@TableName("generic_report")
@Data
public class GenericReport {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @Null(message = "新增时 id 必须为空", groups = ReportInsert.class)
    @NotNull(message = "更新时 id 不允许为空", groups = ReportUpdate.class)
    @NotNull(message = "删除时 id 不允许为空", groups = ReportDelete.class)
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
     * 是否允许编辑，0 允许，1 不允许
     */
    @IncludeValid(value = {"0", "1"}, message = " allowEdit 只有 0 允许，1 不允许", groups = {ReportInsert.class, ReportUpdate.class})
    private Integer allowEdit;
    /**
     * 是否允许新增，0 允许，1 不允许
     */
    @IncludeValid(value = {"0", "1"}, message = " allowAdd 只有 0 允许，1 不允许", groups = {ReportInsert.class, ReportUpdate.class})
    private Integer allowAdd;
    /**
     * 是否允许删除，0 允许，1 不允许
     */
    @IncludeValid(value = {"0", "1"}, message = " allowDelete 只有 0 允许，1 不允许", groups = {ReportInsert.class, ReportUpdate.class})
    private Integer allowDelete;
    /**
     * 是否允许查询，0 允许，1 不允许
     */
    @IncludeValid(value = {"0", "1"}, message = " allowFilter 只有 0 允许，1 不允许", groups = {ReportInsert.class, ReportUpdate.class})
    private Integer allowFilter;
    /**
     * 是否允许导出，0 允许，1 不允许
     */
    @IncludeValid(value = {"0", "1"}, message = " allowExport 只有 0 允许，1 不允许", groups = {ReportInsert.class, ReportUpdate.class})
    private Integer allowExport;
    /**
     * 一页显示多少条数据
     */
    @Min(value = 1, message = "不能低于 1 条", groups = {ReportInsert.class, ReportUpdate.class})
    @Max(value = 999, message = "不能高于 999 条", groups = {ReportInsert.class, ReportUpdate.class})
    private Integer pageSize;
    /**
     * 是否开启图标，0 不开启，1 饼图，2 折线图
     */
    @IncludeValid(value = {"0", "1"}, message = "目前仅支持 0 不开启，1 饼图，2 折线图", groups = {ReportInsert.class, ReportUpdate.class})
    private Integer chartType;
    /**
     * 创建报表设置的用户 id
     */
    @NotNull(message = "用户 id 不能为空", groups = ReportInsert.class)
    @Null(message = "用户 id 不能修改", groups = ReportUpdate.class)
    private Integer createUser;
    /**
     * 创建时间
     */
    @Null(message = "不允许传入创建时间，系统自动创建", groups = {ReportInsert.class, ReportUpdate.class})
    private Timestamp createTime;
    /**
     * 修改报表设置的用户 id
     */
    @NotNull(message = "修改的用户 id 不能为空", groups = {ReportInsert.class, ReportUpdate.class})
    private Integer modifyUser;
    /**
     * 最后修改时间
     */
    @Null(message = "不允许传入修改时间，系统自动创建", groups = {ReportInsert.class, ReportUpdate.class})
    private Timestamp modifyTime;
    /**
     * 接口的 id
     */
    @NotNull(message = "接口 id 不能为空", groups = ReportInsert.class)
    private Integer serviceId;

    public interface ReportInsert {
    }

    public interface ReportUpdate {
    }
    
    public interface ReportDelete {
    }

}