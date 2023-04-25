package cn.youngkbt.generic.base.dto.category;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 20:41
 * @note
 */
@Data
public class CategoryQueryDTO {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 目录名称
     */
    private String categoryCode;
    /**
     * 目录名称
     */
    private String categoryName;
    /**
     * 创建目录的用户 id
     */
    private String createUser;
    /**
     * 项目表 id
     */
    @NotNull(message = "项目 id 不能为空")
    private Integer projectId;
}
