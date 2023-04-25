package cn.youngkbt.generic.utils;

import cn.youngkbt.generic.base.dto.ConditionDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022/12/5 23:01
 * @note
 */
public class SearchUtils {
    
    private SearchUtils() {
    }

    public static <T> QueryWrapper<T> parseWhereSql(List<ConditionDTO> conditionList, Class<T> classObj) {
        if(null == conditionList) {
            return null;
        }
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (!conditionList.isEmpty()) {
            for (ConditionDTO conditionDTO : conditionList) {
                switch (conditionDTO.getType()) {
                    case "eq":
                        queryWrapper.eq(conditionDTO.getColumn(), conditionDTO.getValue());
                        break;
                    case "ne":
                        queryWrapper.ne(conditionDTO.getColumn(), conditionDTO.getValue());
                        break;
                    case "like":
                        queryWrapper.like(conditionDTO.getColumn(), conditionDTO.getValue());
                        break;
                    case "llike":
                        queryWrapper.likeLeft(conditionDTO.getColumn(), conditionDTO.getValue());
                        break;
                    case "rlike":
                        queryWrapper.likeRight(conditionDTO.getColumn(), conditionDTO.getValue());
                        break;
                    case "notlike":
                        queryWrapper.notLike(conditionDTO.getColumn(), conditionDTO.getValue());
                        break;
                    case "gt":
                        queryWrapper.gt(conditionDTO.getColumn(), conditionDTO.getValue());
                        break;
                    case "lt":
                        queryWrapper.lt(conditionDTO.getColumn(), conditionDTO.getValue());
                        break;
                    case "ge":
                        queryWrapper.ge(conditionDTO.getColumn(), conditionDTO.getValue());
                        break;
                    case "le":
                        queryWrapper.le(conditionDTO.getColumn(), conditionDTO.getValue());
                        break;
                    default:
                        break;
                }
            }
        }
        return queryWrapper;
    }
}
