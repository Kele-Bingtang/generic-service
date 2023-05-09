package cn.youngkbt.generic.common.dto.field;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 23:38
 * @note
 */
@Data
public class FieldDeleteDTO {
    /**
     * 主键
     */
    @NotNull(message = "id 不允许为空")
    private Integer id;
}
