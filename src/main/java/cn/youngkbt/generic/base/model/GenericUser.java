package cn.youngkbt.generic.base.model;

import cn.youngkbt.generic.valid.annotation.IncludeValid;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@TableName("generic_user")
@Data
public class GenericUser {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@Null(message = "新增时 id 必须为空", groups = UserInsert.class)
	@NotNull(message = "更新时 id 不允许为空", groups = UserUpdate.class)
	@NotNull(message = "删除时 id 不允许为空", groups = UserDelete.class)
	private Integer id;
	/**
	 * 用户名
	 */
	@NotNull(message = "用户名不允许为空", groups = UserInsert.class)
	private String username;
	/**
	 * 用户昵称
	 */
	private String nickname;
	/**
	 * 密码
	 */
	@NotNull(message = "密码不允许为空", groups = UserInsert.class)
	private String password;
	/**
	 * 邮箱
	 */
	@Email(message = "邮箱格式不正确！", groups = UserInsert.class)
	private String email;
	/**
	 * 性别：0 保密，1 男，2 女
	 */
	@IncludeValid(value = {"0", "1", "2"}, message = "性别 0 无，1 男，2 女，请传入数字", groups = UserUpdate.class)
	private Integer gender;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 手机号码
	 */
	private String phone;
	/**
	 * 状态，0 在线，1 离线
	 */
	@IncludeValid(value = {"0", "1"}, message = "状态 0 在线，1 离线，请传入数字", groups = UserUpdate.class)
	private Integer status;
	/**
	 * 注册时间
	 */
	@NotNull(message = "用户 id 不能为空", groups = UserInsert.class)
	@Null(message = "用户 id 不能修改", groups = UserUpdate.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date registerTime;
	/**
	 * 最后修改时间
	 */
	@Null(message = "不允许传入修改时间，系统自动创建", groups = {GenericService.ServiceInsert.class, GenericService.ServiceUpdate.class})
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date modifyTime;

	/**
	 * 用户的角色信息
	 */
	@TableField(exist = false)
	private GenericRole genericRole;

	public interface UserInsert {
	}
	
	public interface UserUpdate {
	}

	public interface UserDelete {
	}

}