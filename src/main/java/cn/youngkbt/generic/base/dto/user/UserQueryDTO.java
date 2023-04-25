package cn.youngkbt.generic.base.dto.user;

import lombok.Data;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 23:18
 * @note
 */
@Data
public class UserQueryDTO {
    /**
     * 主键
     */
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
     * 头像
     */
    private String avatar;
}
