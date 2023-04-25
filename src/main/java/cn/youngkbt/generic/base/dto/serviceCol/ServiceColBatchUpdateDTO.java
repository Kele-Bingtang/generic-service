package cn.youngkbt.generic.base.dto.serviceCol;

import cn.youngkbt.generic.valid.ValidList;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Kele-Bingtang
 * @date 2023/4/23 20:56
 * @note
 */
@Data
public class ServiceColBatchUpdateDTO {
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
     * 请求名称集合
     */
    @NotNull(message = "批量修改的字段不能为空")
    private ValidList<String> jsonColList;
}
