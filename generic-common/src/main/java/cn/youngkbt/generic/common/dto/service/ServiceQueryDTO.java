package cn.youngkbt.generic.common.dto.service;

import cn.youngkbt.generic.common.valid.annotation.ValidFields;
import lombok.Data;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 22:35
 * @note
 */
@Data
@ValidFields(fields = {"id", "projectId"}, message = "项目或接口不能为空", any = true)
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
