package cn.youngkbt.generic.utils;

/**
 * @author Kele-Bingtang
 * @date 2022/12/12 20:17
 * @note
 */
public class StringUtils {

    private StringUtils() {
    }

    /**
     * 内容是否为空
     *
     * @param content 内容
     * @return true：为空，false：不为空
     */
    public static boolean isBlank(String content) {
        return null == content || "".equals(content);
    }

    public static boolean isBlank(String... content) {
        for (String s : content) {
            if (isBlank(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 内容是否为空
     *
     * @param content 内容
     * @return true：不为空，false：为空
     */
    public static boolean isNotBlank(String content) {
        return !isBlank(content);
    }

    public static boolean isNotBlank(String ...content) {
        return !isBlank(content);
    }

}
