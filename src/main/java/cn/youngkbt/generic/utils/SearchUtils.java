package cn.youngkbt.generic.utils;

import cn.youngkbt.generic.vo.ConditionVo;
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

    public static <T> QueryWrapper<T> parseWhereSql(List<ConditionVo> conditionList, Class<T> classObj) {
        if(null == conditionList) {
            return null;
        }
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (!conditionList.isEmpty()) {
            for (ConditionVo conditionVo : conditionList) {
                switch (conditionVo.getType()) {
                    case "eq":
                        queryWrapper.eq(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "ne":
                        queryWrapper.ne(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "like":
                        queryWrapper.like(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "llike":
                        queryWrapper.likeLeft(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "rlike":
                        queryWrapper.likeRight(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "notlike":
                        queryWrapper.notLike(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "gt":
                        queryWrapper.gt(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "lt":
                        queryWrapper.lt(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "ge":
                        queryWrapper.ge(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    case "le":
                        queryWrapper.le(conditionVo.getColumn(), conditionVo.getValue());
                        break;
                    default:
                        break;
                }
            }
        }
        return queryWrapper;
    }
}
