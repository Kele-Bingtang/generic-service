package cn.youngkbt.generic.base.dto.service;

import cn.youngkbt.generic.valid.annotation.IncludeValid;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 22:35
 * @note
 */
@Data
public class ServiceInsertDTO {
    /**
     * 接口名
     */
    @NotNull(message = "接口名不能为空")
    private String serviceName;
    /**
     * 接口地址
     */
    @NotNull(message = "接口地址不能为空")
    private String serviceUrl;
    /**
     * 接口完整地址
     */
    @NotNull(message = "接口完整地址不能为空")
    private String fullUrl;
    /**
     * 接口状态，0 禁用，1 启用
     */
    @IncludeValid(value = {"0", "1"}, message = "接口状态不能为空")
    private Integer status;
    /**
     * 接口描述
     */
    private String serviceDesc;
    /**
     * 接口的查询 SQL 语句
     */
    private String selectSql;
    /**
     * 执行查询语句的表名
     */
    private String selectTable;
    /**
     * 执行更新语句的表名
     */
    private String updateTable;
    /**
     * 执行插入语句的表名
     */
    private String insertTable;
    /**
     * 执行删除语句的表名
     */
    private String deleteTable;
    /**
     * 创建接口的用户 id
     */
    @NotNull(message = "用户名不能为空")
    private String createUser;
    /**
     * 修改接口的用户 id
     */
    @NotNull(message = "修改的用户名不能为空")
    private String modifyUser;
    /**
     * 项目的 id
     */
    @NotNull(message = "项目 id 不能为空")
    private Integer projectId;
    /**
     * 目录的 id
     */
    @NotNull(message = "目录 id 不能为空")
    private Integer categoryId;
    /**
     * 报表名称
     */
    private String reportTitle;
}
