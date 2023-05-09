package cn.youngkbt.generic.common.dto.project;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 22:10
 * @note
 */
@Data
public class ProjectDeleteDTO {
    /**
     * 主键
     */
    @NotNull(message = "id 不允许为空")
    private Integer id;
}
