package cn.youngkbt.generic.base.dto.category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 20:44
 * @note
 */
public class CategoryUpdateDTO {
    /**
     * 主键
     */
    @NotNull(message = "更新时 id 不允许为空")
    private Integer id;
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
}
