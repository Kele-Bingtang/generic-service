package cn.youngkbt.generic.vo;

import lombok.Data;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 21:32
 * @note
 */
@Data
public class ProjectVO {
    /**
     * 主键
     */
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
}
