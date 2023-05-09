package cn.youngkbt.generic.common.dto.service;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 22:36
 * @note
 */
@Data
public class ServiceUpdateDTO {
    /**
     * 主键
     */
    @NotNull(message = "更新时 id 不允许为空")
    private Integer id;
    /**
     * 接口名
     */
    private String serviceName;
    /**
     * 接口地址
     */
    private String serviceUrl;
    /**
     * 接口完整地址
     */
    private String fullUrl;
    /**
     * 接口状态，0 禁用，1 启用
     */
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
     * 是否进行认证，0 不认证，1 认证
     */
    private Integer isAuth;
    /**
     * 执行删除语句的表名
     */
    private String deleteTable;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改接口的用户 id
     */
    @NotNull(message = "修改的用户名不能为空")
    private String modifyUser;
    /**
     * 报表名称
     */
    private String reportTitle;
}
