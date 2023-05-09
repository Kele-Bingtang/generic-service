package cn.youngkbt.generic.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 22:36
 * @note
 */
@Data
public class ServiceVO {
    /**
     * 主键
     */
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
     * 执行删除语句的表名
     */
    private String deleteTable;
    /**
     * 是否进行认证，0 不认证，1 认证
     */
    private Integer isAuth;
    /**
     * 创建接口的用户 id
     */
    private String createUser;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 报表名称
     */
    private String reportTitle;
}
