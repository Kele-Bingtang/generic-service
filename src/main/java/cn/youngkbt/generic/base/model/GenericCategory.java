package cn.youngkbt.generic.base.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Kele-Bingtang
 * @note 1.0
 * @date 2022-12-03 22:45:22
 */
@TableName("generic.category")
@Data
public class GenericCategory {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
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
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改目录的用户 id
     */
    private String modifyUser;
    /**
     * 最后修改时间
     */
    private Date modifyTime;
    /**
     * 项目表 id
     */
    private Integer projectId;
}