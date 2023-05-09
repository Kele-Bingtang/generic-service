package cn.youngkbt.generic.common.dto.field;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 23:39
 * @note
 */
@Data
public class FieldInsertDTO {
    /**
     * 项目名
     */
    @NotBlank(message = "属性名不能为空")
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
     * 创建项目的用户 id
     */
    @NotNull(message = "用户名不能为空")
    private String createUser;
    /**
     * 修改项目的用户 id
     */
    @NotNull(message = "修改的用户名不能为空")
    private String modifyUser;
    /**
     * 接口的 id
     */
    @NotNull(message = "接口 id 不能为空")
    private Integer	serviceId;
}
