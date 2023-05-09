package cn.youngkbt.generic.common.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 23:18
 * @note
 */
@Data
public class UserDeleteDTO {
    /**
     * 用户名
     */
    @NotNull(message = "用户名不允许为空")
    private String username;
}
