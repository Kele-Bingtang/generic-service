package cn.youngkbt.generic.common.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Kele-Bingtang
 * @note 1.0
 * @date 2022-12-03 22:45:22
 */
@TableName("generic.report")
@Data
public class GenericReport {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
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
     * 创建报表设置的用户 id
     */
    private String createUser;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改报表设置的用户 id
     */
    private String modifyUser;
    /**
     * 最后修改时间
     */
    private Date modifyTime;
    /**
     * 接口的 id
     */
    private Integer serviceId;

}