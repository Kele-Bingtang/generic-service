package cn.youngkbt.generic.base.dto.service;

import lombok.Data;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 22:35
 * @note
 */
@Data
public class ServiceQueryDTO {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 接口名
     */
    private String serviceName;
    /**
     * 接口地址
     */
    private String serviceUrl;
    /**
     * 项目的 id
     */
    private Integer projectId;
    /**
     * 目录的 id
     */
    private Integer categoryId;
}
