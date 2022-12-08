package cn.youngkbt.base.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.sql.Timestamp;

/**
 * @author Kele-Bingtang
 * @version 1.0
 * @since 2022-12-03 22:45:22
 */
@TableName("generic_project")
@Data
public class GenericProject {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @Null(message = "新增时 id 必须为空", groups = ProjectInsert.class)
    @NotNull(message = "更新时 id 不允许为空", groups = ProjectUpdate.class)
    @NotNull(message = "删除时 id 不允许为空", groups = ProjectDelete.class)
    private Integer id;
    /**
     * 项目名
     */
    @NotBlank(message = "项目名不能为空", groups = ProjectInsert.class)
    private String projectName;
    /**
     * 接口基础路径
     */
    @NotBlank(message = "基础接口路径不能为空", groups = ProjectInsert.class)
    private String baseUrl;
    /**
     * 项目描述
     */
    private String description;
    /**
     * 项目密钥，唯一
     */
    @Null(message = "密钥不允许传入", groups = {ProjectInsert.class, ProjectUpdate.class})
    private String secretKey;
    /**
     * 数据库名称
     */
    @Null(message = "数据库不能为空", groups = {ProjectInsert.class})
    private String databaseName;
    /**
     * 创建项目的用户 id
     */
    @NotNull(message = "用户 id 不能为空", groups = ProjectInsert.class)
    @Null(message = "用户 id 不能修改", groups = ProjectUpdate.class)
    private Integer createUser;
    /**
     * 创建时间
     */
    @Null(message = "不允许传入创建时间，系统自动创建", groups = {ProjectInsert.class, ProjectUpdate.class})
    private Timestamp createTime;
    /**
     * 修改项目的用户 id
     */
    @NotNull(message = "修改的用户 id 不能为空", groups = {ProjectInsert.class, ProjectUpdate.class})
    private Integer modifyUser;
    /**
     * 最后修改时间
     */
    @Null(message = "不允许传入修改时间，系统自动创建", groups = {ProjectInsert.class, ProjectUpdate.class})
    private Timestamp modifyTime;

    public interface ProjectInsert {
    }

    public interface ProjectUpdate {
    }

    public interface ProjectDelete {
    }

}