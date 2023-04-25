package cn.youngkbt.generic.base.dto.category;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 21:14
 * @note
 */
@Data
public class CategoryDeleteDTO {

    /**
     * 主键
     */
    @NotNull(message = "删除时 id 不允许为空")
    private Integer id;
}
