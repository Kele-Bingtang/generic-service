package cn.youngkbt.generic.common.dto.project;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 22:02
 * @note
 */
@Data
public class ProjectUpdateDTO {

    /**
     * 主键
     */
    @NotNull(message = "更新时 id 不允许为空")
    private Integer id;
    /**
     * 项目名
     */
    @NotNull(message = "项目名不允许为空")
    private String projectName;
    /**
     * 接口基础路径
     */
    @NotNull(message = "接口基础路径不允许为空")
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
     * 修改项目的用户 id
     */
    @NotNull(message = "修改的用户名不能为空")
    private String modifyUser;
}
