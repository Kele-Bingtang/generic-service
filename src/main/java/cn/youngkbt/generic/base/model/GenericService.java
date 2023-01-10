package cn.youngkbt.generic.base.model;

import cn.youngkbt.generic.valid.annotation.IncludeValid;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@TableName("generic_service")
@Data
public class GenericService {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@Null(message = "新增时 id 必须为空", groups = ServiceInsert.class)
	@NotNull(message = "更新时 id 不允许为空", groups = ServiceUpdate.class)
	@NotNull(message = "删除时 id 不允许为空", groups = ServiceDelete.class)
	private Integer id;
	/**
	 * 接口名
	 */
	@TableField(condition = SqlCondition.LIKE)
	@NotNull(message = "接口名不能为空", groups = ServiceInsert.class)
	private String serviceName;
	/**
	 * 接口地址
	 */
	@TableField(condition = SqlCondition.LIKE)
	@NotNull(message = "接口地址不能为空", groups = ServiceInsert.class)
	private String serviceUrl;
	/**
	 * 接口完整地址
	 */
	@NotNull(message = "接口完整地址不能为空", groups = {ServiceInsert.class, ServiceUpdate.class})
	private String fullUrl;
	/**
	 * 接口状态，0 启用，1 禁用
	 */
	@IncludeValid(value = {"0", "1"}, message = "接口状态不能为空", groups = ServiceInsert.class)
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
	@NotNull(message = "用户名不能为空", groups = ServiceInsert.class)
	@Null(message = "用户名不能修改", groups = ServiceUpdate.class)
	private String createUser;
	/**
	 * 创建时间
	 */
	@Null(message = "不允许传入创建时间，系统自动创建", groups = {ServiceInsert.class, ServiceUpdate.class})
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 修改接口的用户 id
	 */
	@NotNull(message = "修改的用户名不能为空", groups = {ServiceInsert.class, ServiceUpdate.class})
	private String modifyUser;
	/**
	 * 最后修改时间
	 */
	@Null(message = "不允许传入修改时间，系统自动创建", groups = {ServiceInsert.class, ServiceUpdate.class})
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date modifyTime;
	/**
	 * 项目的 id
	 */
	@NotNull(message = "项目 id 不能为空", groups = ServiceInsert.class)
	private Integer projectId;
	/**
	 * 目录的 id
	 */
	@NotNull(message = "目录 id 不能为空", groups = ServiceInsert.class)
	private Integer categoryId;

	/**
	 * 报表名称
	 */
	private String reportTitle;

	public interface ServiceInsert {
	}

	public interface ServiceUpdate {
	}

	public interface ServiceDelete {
	}

}