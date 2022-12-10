package cn.youngkbt.generic.utils;

import java.util.UUID;

/**
 * @author Kele-Bingtang
 * @date 2022/12/4 16:46
 * @note
 */
public class SecureUtil {
    
    public static String noBarUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    public static String simpleUUID() {
        return UUID.randomUUID().toString();
    }
}
