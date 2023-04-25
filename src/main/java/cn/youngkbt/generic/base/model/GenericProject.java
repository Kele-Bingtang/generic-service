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
@TableName("generic.project")
@Data
public class GenericProject {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 项目名
     */
    private String projectName;
    /**
     * 接口基础路径
     */
    private String baseUrl;
    /**
     * 项目描述
     */
    private String description;
    /**
     * 项目密钥，唯一
     */
    private String secretKey;
    /**
     * 数据库名称
     */
    private String databaseName;
    /**
     * 创建项目的用户 id
     */
    private String createUser;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改项目的用户 id
     */
    private String modifyUser;
    /**
     * 最后修改时间
     */
    private Date modifyTime;

}