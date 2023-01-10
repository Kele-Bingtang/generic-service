package cn.youngkbt.generic.vo;

import cn.youngkbt.generic.vo.annotation.Convert;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@Data
public class GenericServiceVo {
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
	 * 接口状态，0 启用，1 禁用
	 */
	@Convert(source = {0, 1 }, target = {"启用", "禁用"})
	private String status;
	/**
	 * 接口描述
	 */
	private String serviceDesc;
	/**
	 * 接口的 SQL 语句
	 */
	private String selectSql;
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
	private String createUser;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 修改接口的用户 id
	 */
	private String modifyUser;
	/**
	 * 最后修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date modifyTime;
	/**
	 * 报表名称
	 */
	private String reportTitle;
}