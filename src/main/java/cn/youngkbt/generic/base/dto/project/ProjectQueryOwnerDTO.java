package cn.youngkbt.generic.base.dto.project;

import lombok.Data;

/**
 * @author Kele-Bingtang
 * @note 1.0
 * @date 2022-12-03 22:45:22
 */
@Data
public class ProjectQueryOwnerDTO {
    /**
     * 项目类型：0 自己创建的，1 加入其他人的，null 则是自己所有的项目
     */
    private Integer enterType;

}