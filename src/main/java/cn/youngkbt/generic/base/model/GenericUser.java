package cn.youngkbt.generic.base.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@TableName("generic.user")
@Data
public class GenericUser {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 用户昵称
	 */
	private String nickname;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 性别：0 保密，1 男，2 女
	 */
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
	 * 状态，0 离线，1 在线
	 */
	private Integer status;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 注册时间
	 */
	private Date registerTime;
	/**
	 * 最后修改时间
	 */
	private Date modifyTime;

	/**
	 * 用户的角色信息
	 */
	@TableField(exist = false)
	private GenericRole genericRole;

}