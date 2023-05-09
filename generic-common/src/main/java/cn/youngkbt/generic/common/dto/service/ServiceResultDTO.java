package cn.youngkbt.generic.common.dto.service;

import cn.youngkbt.generic.common.dto.serviceCol.ServiceColResultDTO;
import lombok.Data;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2023/4/27 23:40
 * @note
 */
@Data
public class ServiceResultDTO {
    private Integer serviceId;
    private String serviceName;
    private List<ServiceColResultDTO> serviceColList;
}
