package cn.youngkbt.generic.common.utils;

import java.util.UUID;

/**
 * @author Kele-Bingtang
 * @date 2022/12/12 20:17
 * @note 字符串相关工具类
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
            if (!isBlank(s)) {
                return false;
            }
        }
        return true;
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

    public static String columnToLowerCamelCase(String column) {
        StringBuilder charUpper = new StringBuilder();
        String[] splitName = column.toLowerCase().split("_");
        charUpper.append(splitName[0]);
        for (int i = 1; i < splitName.length; i++) {
            if (null != splitName[i]) {
                String s = firstCharToUpperCase(splitName[i]);
                charUpper.append(s);
            }
        }
        return charUpper.toString();
    }
    public static String firstCharToUpperCase(String fieldName) {
        char[] chars = fieldName.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] -= 32;
            return String.valueOf(chars);
        } else {
            return fieldName;
        }
    }

    public static String noBarUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String simpleUUID() {
        return UUID.randomUUID().toString();
    }

}
