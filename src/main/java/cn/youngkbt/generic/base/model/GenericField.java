package cn.youngkbt.generic.base.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 23:28
 * @note
 */
@TableName(value = "generic.field")
@Data
public class GenericField {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String field;
    private String parentField;
    private String description;
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> jsonCol;
    private String createUser;
    private Date createTime;
    private String modifyUser;
    private Date modifyTime;
    private Integer serviceId;
}
