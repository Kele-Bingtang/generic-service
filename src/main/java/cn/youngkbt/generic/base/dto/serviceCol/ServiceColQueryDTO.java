package cn.youngkbt.generic.base.dto.serviceCol;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 23:01
 * @note
 */
@Data
public class ServiceColQueryDTO {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 表字段名称
     */
    private String tableCol;
    /**
     * 请求返回的 JSON 字段名称
     */
    private String jsonCol;
    /**
     * 报表字段名称
     */
    private String reportCol;
    /**
     * 增删改时，是否作为 where 条件，0 不作为，1 作为（值为空传）2 作为（值为空不传）
     */
    private Integer isWhereKey;
    /**
     * 是否允许插入，0 不允许，1 允许
     */
    private Integer allowInsert;
    /**
     * 是否允许更新，0 不允许，1 允许
     */
    private Integer allowUpdate;
    /**
     * 是否允许查询，0 不允许，1 允许
     */
    private Integer allowFilter;
    /**
     * 是否允许返回在请求里，0 不允许，1 允许
     */
    private Integer allowRequest;
    /**
     * 是否允许出现在报表，0 不允许，1 允许
     */
    private Integer allowShowInReport;
    /**
     * 报表字段出现的顺序
     */
    private Integer displaySeq;
    /**
     * 接口的 id
     */
    @NotNull(message = "接口 id 不能为空")
    private Integer	serviceId;
}
