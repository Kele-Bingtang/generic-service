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

}
