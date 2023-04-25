package cn.youngkbt.generic.base.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Kele-Bingtang
 * @date 2022/12/10 21:32
 * @note
 */
@Data
@TableName("generic.role")
public class GenericRole {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String code;

    private String name;

    private String createUser;
  
    private Date createTime;
  
    private String modifyUser;
  
    private Date modifyTime;
}
