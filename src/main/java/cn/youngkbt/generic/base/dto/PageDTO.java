package cn.youngkbt.generic.base.dto;

import lombok.Data;

/**
 * @author Kele-Bingtang
 * @date 2023/2/11 14:19
 * @note
 */
@Data
public class PageDTO {
    /**
     * 当前页
     */
    private Integer pageNo;
    /**
     * 一个显示多少数据
     */
    private Integer pageSize;
    
}
