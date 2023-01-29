package cn.youngkbt.generic.base.model;

import cn.youngkbt.generic.valid.annotation.IncludeValid;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("service_col")
@Data
public class ServiceCol {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@Null(message = "新增时 id 必须为空", groups = ServiceColInsert.class)
	@NotNull(message = "更新时 id 不允许为空", groups = ServiceColUpdate.class)
	@NotNull(message = "删除时 id 不允许为空", groups = ServiceColDelete.class)
	private Integer id;
	/**
	 * 表字段名称
	 */
	private String tableCol;
	/**
	 * 请求返回的 JSON 字段名称
	 */
	private String jsonCol;
	/**
	 * 报表字段名称
	 */
	private String reportCol;
	/**
	 * 字段类型
	 */
	private String colType;
	/**
	 * 字段类型长度
	 */
	private Integer colLength;
	/**
	 * 增删改时，是否作为 where 条件，0 不作为，1 作为（不一定是主键）
	 */
	@IncludeValid(value = {"0", "1"}, message = "是否作为 where 条件，0 不作为，1 作为，请传入数字", groups = ServiceColUpdate.class)
	private Integer isWhereKey;
	/**
	 * 字段默认值
	 */
	private String defaultValue;
	/**
	 * 数据是否加密，0 不加密，1 加密
	 */
	@IncludeValid(value = {"0", "1"}, message = "数据是否加密，0 不加密，1 加密，请传入数字", groups = ServiceColUpdate.class)
	private Integer dataEncrypt;
	/**
	 * select 时的筛选，如 where 字段 = xx、like %xx%。0 为不筛选，1 为 = 精准匹配，2 为 like %xx，3 为 like xx%，4 为 like %xx%，5 为 like xx。如果为 5，你可以变成 2、3、4，即 xx 可以加 % 或者 _ 或者 []，只要符合 like 的要求即可。
	 */
	private Integer queryFilter;
	/**
	 * 排序，格式：x-x，前面表示 order by 的顺序，后者表示升序或者降序，只有 0(asc) 和 1(desc)。如：name 字段为 0-0，age 字段为 1-1，则表示 order by name asc, age desc
	 */
	private String orderBy;
	/**
	 * 是否允许插入，0 不允许，1 允许
	 */
	@IncludeValid(value = {"0", "1"}, message = "是否允许插入，0 不允许，1 允许，请传入数字", groups = ServiceColUpdate.class)
	private Integer allowInsert;
	/**
	 * 是否允许更新，0 不允许，1 允许
	 */
	@IncludeValid(value = {"0", "1"}, message = "是否允许更新，0 不允许，1 允许，请传入数字", groups = ServiceColUpdate.class)
	private Integer allowUpdate;
	/**
	 * 是否允许查询，0 不允许，1 允许
	 */
	@IncludeValid(value = {"0", "1"}, message = "是否允许查询，0 不允许，1 允许，请传入数字", groups = ServiceColUpdate.class)
	private Integer allowFilter;
	/**
	 * 是否允许出现在报表，0 不允许，1 允许
	 */
	@IncludeValid(value = {"0", "1"}, message = "是否允许出现在报表，0 不允许，1 允许，请传入数字", groups = ServiceColUpdate.class)
	private Integer allowShowInReport;
	/**
	 * 是否允许出现在报表的增删改弹出框，0 不允许，1 允许
	 */
	@IncludeValid(value = {"0", "1"}, message = "是否允许出现在报表的增删改弹出框，0 不允许，1 允许，请传入数字", groups = ServiceColUpdate.class)
	private Integer allowShowInDetail;
	/**
	 * 报表字段出现的顺序
	 */
	private Integer displaySeq;
	/**
	 * 报表字段显示的宽度，-1 为 auto，其他为准确的数值 + px
	 */
	private Integer reportColWidth;
	/**
	 * 报表的增删改弹出框，该字段的输入框宽度，-1 为 auto，其他为准确的数值+px
	 */
	private Integer detailColWidth;
	/**
	 * 列对齐，0 为左对齐，1 为居中，2 为右对齐
	 */
	@IncludeValid(value = {"0", "1", "2"}, message = "列对齐，0 为左对齐，1 为居中，2 为右对齐，请传入数字", groups = ServiceColUpdate.class)
	private Integer colAlign;
	/**
	 * 创建表的用户
	 */
	@NotNull(message = "用户名不能为空", groups = ServiceColInsert.class)
	@Null(message = "用户名不能修改", groups = ServiceColUpdate.class)
	private String createUser;
	/**
	 * 创建时间
	 */
	@Null(message = "不允许传入创建时间，系统自动创建", groups = {ServiceColInsert.class, ServiceColUpdate.class})
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 修改表的用户
	 */
	@NotNull(message = "修改的用户名不能为空", groups = {ServiceColInsert.class, ServiceColUpdate.class})
	private String modifyUser;
	/**
	 * 最后修改时间
	 */
	@Null(message = "不允许传入修改时间，系统自动创建", groups = {ServiceColInsert.class, ServiceColUpdate.class})
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date modifyTime;
	/**
	 * 接口的 id
	 */
	@NotNull(message = "接口 id 不能为空", groups = ServiceColInsert.class)
	@NotNull(message = "接口 id 不能为空")
	private Integer	serviceId;

	public interface ServiceColInsert {
	}

	public interface ServiceColUpdate {
	}

	public interface ServiceColDelete {
	}

}