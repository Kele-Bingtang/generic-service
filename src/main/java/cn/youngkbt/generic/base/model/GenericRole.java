package cn.youngkbt.generic.base.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * @author Kele-Bingtang
 * @date 2022/12/10 21:32
 * @note
 */
@Data
@TableName("generic_role")
public class GenericRole {

    @TableId(type = IdType.AUTO)
    @Null(message = "新增时 id 必须为空", groups = RoleInsert.class)
    @NotNull(message = "更新时 id 不允许为空", groups = RoleUpdate.class)
    @NotNull(message = "删除时 id 不允许为空", groups = RoleDelete.class)
    private Integer id;

    @NotBlank(message = "角色码不能为空", groups = {RoleInsert.class, RoleUpdate.class})
    private String code;

    @NotBlank(message = "角色名称不能为空", groups = {RoleInsert.class, RoleUpdate.class})
    private String name;

    @NotNull(message = "用户名不能为空", groups = RoleInsert.class)
    @Null(message = "用户名不能修改", groups = RoleUpdate.class)
    private String createUser;
  
    @Null(message = "不允许传入创建时间，系统自动创建", groups = {RoleInsert.class, RoleUpdate.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
  
    @NotNull(message = "修改的用户名不能为空", groups = {RoleInsert.class, RoleUpdate.class})
    private String modifyUser;
  
    @Null(message = "不允许传入修改时间，系统自动创建", groups = {RoleInsert.class, RoleUpdate.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;
    
    interface RoleInsert {
    }

    interface RoleUpdate {
    }
    
    interface RoleDelete {
    }
}
