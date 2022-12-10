package cn.youngkbt.generic.utils;

/**
 * @author Kele-Bingtang
 * @date 2022/12/5 23:04
 * @note
 */
public class StringUtil {

    /**
     * 内容是否为空
     *
     * @param content 内容
     * @return true：为空，false：不为空
     */
    public static boolean isEmpty(Object content) {
        return null == content || "".equals(content);
    }

    /**
     * 内容是否为空
     *
     * @param content 内容
     * @return true：不为空，false：为空
     */
    public static boolean isNotEmpty(Object content) {
        return !isEmpty(content);
    }
    
}
