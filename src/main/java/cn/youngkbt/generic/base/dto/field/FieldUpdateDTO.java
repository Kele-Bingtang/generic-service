package cn.youngkbt.generic.base.dto.field;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 23:43
 * @note
 */
@Data
public class FieldUpdateDTO {
    /**
     * 主键
     */
    @NotNull(message = "更新时 id 不允许为空")
    private Integer id;
    /**
     * 项目名
     */
    @NotBlank(message = "项目名不能为空")
    private String field;
    /**
     * 接口基础路径
     */
    private String parentField;

    /**
     * 项目描述
     */
    private String description;
    /**
     * 数据库名称
     */
    private List<String> jsonCol;
    /**
     * 修改项目的用户 id
     */
    @NotNull(message = "修改的用户名不能为空")
    private String modifyUser;
}
