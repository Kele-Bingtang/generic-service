package cn.youngkbt.generic.common.dto.report;

import lombok.Data;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 22:15
 * @note
 */
@Data
public class ReportQueryDTO {
    /**
     * 主键
     */
    private Integer serviceId;
    /**
     * 报表名称
     */
    private String reportTitle;
}
