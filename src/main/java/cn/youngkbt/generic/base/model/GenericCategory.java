package cn.youngkbt.generic.base.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * @author Kele-Bingtang
 * @note 1.0
 * @date 2022-12-03 22:45:22
 */
@TableName("generic_category")
@Data
public class GenericCategory {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @Null(message = "新增时 id 必须为空", groups = CategoryInsert.class)
    @NotNull(message = "更新时 id 不允许为空", groups = CategoryUpdate.class)
    @NotNull(message = "删除时 id 不允许为空", groups = CategoryDelete.class)
    private Integer id;
    /**
     * 目录名称
     */
    @NotBlank(message = "目录编码不能为空", groups = {CategoryInsert.class, CategoryUpdate.class})
    private String categoryCode;
    /**
     * 目录名称
     */
    @NotBlank(message = "目录名称不能为空", groups = {CategoryInsert.class, CategoryUpdate.class})
    private String categoryName;
    /**
     * 创建目录的用户 id
     */
    @NotNull(message = "用户名不能为空", groups = CategoryInsert.class)
    @Null(message = "用户名不能修改", groups = CategoryUpdate.class)
    private String createUser;
    /**
     * 创建时间
     */
    @Null(message = "不允许传入创建时间，系统自动创建", groups = {CategoryInsert.class, CategoryUpdate.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 修改目录的用户 id
     */
    @NotNull(message = "修改的用户名不能为空", groups = {CategoryInsert.class, CategoryUpdate.class})
    private String modifyUser;
    /**
     * 最后修改时间
     */
    @Null(message = "不允许传入修改时间，系统自动创建", groups = {CategoryInsert.class, CategoryUpdate.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;
    /**
     * 项目表 id
     */
    @NotNull(message = "项目 id 不能为空", groups = CategoryInsert.class)
    private Integer projectId;

    public interface CategoryInsert {
    }

    public interface CategoryUpdate {
    }

    public interface CategoryDelete {
    }
}