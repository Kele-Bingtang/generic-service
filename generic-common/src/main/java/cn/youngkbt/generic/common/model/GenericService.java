package cn.youngkbt.generic.common.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@TableName("generic.service")
@Data
public class GenericService {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 接口名
	 */
	@TableField(condition = SqlCondition.LIKE)
	private String serviceName;
	/**
	 * 接口地址
	 */
	@TableField(condition = SqlCondition.LIKE)
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
	private Date createTime;
	/**
	 * 修改接口的用户 id
	 */
	private String modifyUser;
	/**
	 * 最后修改时间
	 */
	private Date modifyTime;
	/**
	 * 项目的 id
	 */
	private Integer projectId;
	/**
	 * 目录的 id
	 */
	private Integer categoryId;

	/**
	 * 报表名称
	 */
	private String reportTitle;

}