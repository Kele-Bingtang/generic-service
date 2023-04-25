package cn.youngkbt.generic.base.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 23:47
 * @note
 */
@Data
public class FieldVO {
    private Integer id;
    private String field;
    private String parentField;
    private String description;
    private List<String> jsonCol;
    private Date createTime;
}
