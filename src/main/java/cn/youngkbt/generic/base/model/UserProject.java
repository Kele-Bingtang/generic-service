package cn.youngkbt.generic.base.model;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author kele
 * @date 2022-12-18 21:00:25
 * @note 1.0
 */
@Data
public class UserProject {
	/**
	 * 主键
 	 */ 
	private Integer id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 项目表 id
	 */
	private Integer projectId;
	/**
	 * 角色表 id
	 */
	private Integer roleId;
	/**
	 * 项目是用户创建的还是加入的：0 创建，1 加入
	 */
	private Integer enterType;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 最后修改时间
	 */
	private Timestamp modifyTime;
	
}