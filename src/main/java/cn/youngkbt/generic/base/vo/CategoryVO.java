package cn.youngkbt.generic.base.vo;

import lombok.Data;

/**
 * @author Kele-Bingtang
 * @date 2023/2/9 20:48
 * @note
 */
@Data
public class CategoryVO {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 目录名称
     */
    private String categoryCode;
    /**
     * 目录名称
     */
    private String categoryName;
}
