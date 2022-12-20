package cn.youngkbt.generic.vo;

import cn.youngkbt.generic.vo.annotation.Convert;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@TableName("generic_user")
@Data
public class GenericUserVo {
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 用户昵称
	 */
	private String nickname;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 性别：0 无，1 男，2 女
	 */
	@Convert(source = {0, 1, 2 }, target = {"无", "男", "女"})
	private String gender;
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
	@Convert(source = {0, 1 }, target = {"在线", "离线"})
	private String status;
	/**
	 * 注册时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date registerTime;
	/**
	 * 最后修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date modifyTime;
	
	private GenericRoleVo role;
}