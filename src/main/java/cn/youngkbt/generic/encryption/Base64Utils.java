package cn.youngkbt.generic.encryption;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 21:03
 * @note
 */
public class Base64Utils {

    /**
     * BASE64解密
     */
    public static String decryBase64(String key) throws Exception {
        return new String((new BASE64Decoder()).decodeBuffer(key));
    }

    /**
     * BASE64加密
     */
    public static String encryptBase64(String key) {
        return (new BASE64Encoder()).encode(key.getBytes());
    }
}
