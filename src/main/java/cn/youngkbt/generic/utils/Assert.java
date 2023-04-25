package cn.youngkbt.generic.utils;

import cn.youngkbt.generic.exception.GenericException;
import cn.youngkbt.generic.http.ResponseStatusEnum;
import org.springframework.lang.Nullable;

/**
 * @author Kele-Bingtang
 * @date 2023/2/6 21:29
 * @note
 */
public class Assert {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new GenericException(ResponseStatusEnum.ERROR.getCode(), ResponseStatusEnum.ERROR.getStatus(), message);
        }
    }

    public static void isTrue(boolean expression, ResponseStatusEnum responseStatusEnum) {
        if (!expression) {
            throw new GenericException(responseStatusEnum);
        }
    }

    public static void isNull(@Nullable Object object, String message) {
        if (object != null) {
            throw new GenericException(ResponseStatusEnum.ERROR.getCode(), ResponseStatusEnum.ERROR.getStatus(), message);
        }
    }

    public static void isNull(@Nullable Object object, ResponseStatusEnum responseStatusEnum) {
        if (object != null) {
            throw new GenericException(responseStatusEnum);
        }
    }

    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new GenericException(ResponseStatusEnum.ERROR.getCode(), ResponseStatusEnum.ERROR.getStatus(), message);
        }
    }

    public static void notNull(@Nullable Object object, ResponseStatusEnum responseStatusEnum) {
        if (object == null) {
            throw new GenericException(responseStatusEnum);
        }
    }

    public static void notBlank(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new GenericException(ResponseStatusEnum.ERROR.getCode(), ResponseStatusEnum.ERROR.getStatus(), message);
        }
    }

    public static void notBlank(String text, ResponseStatusEnum responseStatusEnum) {
        if (StringUtils.isBlank(text)) {
            throw new GenericException(responseStatusEnum);
        }
    }

    public static void isBlank(String text, String message) {
        if (StringUtils.isNotBlank(text)) {
            throw new GenericException(ResponseStatusEnum.ERROR.getCode(), ResponseStatusEnum.ERROR.getStatus(), message);
        }
    }

    public static void isBlank(String text, ResponseStatusEnum responseStatusEnum) {
        if (StringUtils.isNotBlank(text)) {
            throw new GenericException(responseStatusEnum);
        }
    }

}
