package cn.youngkbt.generic.base.dto.user;

import cn.youngkbt.generic.valid.annotation.IncludeValid;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 23:18
 * @note
 */
@Data
public class UserInsertDTO {
    /**
     * 用户名
     */
    @NotNull(message = "用户名不允许为空")
    private String username;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 密码
     */
    @NotNull(message = "密码不允许为空")
    private String password;
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确！")
    private String email;
    /**
     * 性别：0 保密，1 男，2 女
     */
    @IncludeValid(value = {"0", "1", "2"}, message = "性别：0 保密，1 男，2 女，请传入数字")
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
}
