package cn.youngkbt.generic.encryption;

import java.security.MessageDigest;

/**
 * @author Kele-Bingtang
 * @date 2023/4/10 21:13
 * @note
 */
public class MD5Utils {
    public static final String KEY_MD5 = "MD5";

    public static String encryptMD5(String strs) {
        /*
         * 加密需要使用JDK中提供的类
         */
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bs = digest.digest(strs.getBytes());
            /*
             *  加密后的数据是-128 到 127 之间的数字，这个数字也不安全。
             *   取出每个数组的某些二进制位进行某些运算，得到一个具体的加密结果
             *
             *   0000 0011 0000 0100 0010 0000 0110 0001
             *  &0000 0000 0000 0000 0000 0000 1111 1111
             *  ---------------------------------------------
             *   0000 0000 0000 0000 0000 0000 0110 0001
             *   把取出的数据转成十六进制数
             */
            for (byte b : bs) {
                int x = b & 255;
                String s = Integer.toHexString(x);
                if (x > 0 && x < 16) {
                    sb.append("0");
                    sb.append(s);
                } else {
                    sb.append(s);
                }
            }
        } catch (Exception e) {
            return sb.toString();
        }
        return sb.toString();
    }
}
