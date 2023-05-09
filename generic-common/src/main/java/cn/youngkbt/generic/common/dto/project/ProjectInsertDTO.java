package cn.youngkbt.generic.common.dto.project;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 21:44
 * @note
 */
@Data
public class ProjectInsertDTO {
    /**
     * 项目名
     */
    @NotBlank(message = "项目名不能为空")
    private String projectName;
    /**
     * 接口基础路径
     */
    @NotBlank(message = "基础接口路径不能为空")
    private String baseUrl;
    /**
     * 项目描述
     */
    private String description;
    /**
     * 数据库名称
     */
    private String databaseName;
    /**
     * 创建项目的用户 id
     */
    @NotNull(message = "用户名不能为空")
    private String createUser;
    /**
     * 修改项目的用户 id
     */
    @NotNull(message = "修改的用户名不能为空")
    private String modifyUser;
}
