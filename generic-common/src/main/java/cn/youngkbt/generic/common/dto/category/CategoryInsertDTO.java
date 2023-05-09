package cn.youngkbt.generic.common.dto.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 21:04
 * @note
 */
@Data
public class CategoryInsertDTO {
    /**
     * 目录名称
     */
    @NotBlank(message = "目录编码不能为空")
    private String categoryCode;
    /**
     * 目录名称
     */
    @NotBlank(message = "目录名称不能为空")
    private String categoryName;
    /**
     * 创建目录的用户 id
     */
    @NotNull(message = "用户名不能为空")
    private String createUser;
    /**
     * 修改目录的用户 id
     */
    @NotNull(message = "修改的用户名不能为空")
    private String modifyUser;
    /**
     * 项目表 id
     */
    @NotNull(message = "项目 id 不能为空")
    private Integer projectId;
}
