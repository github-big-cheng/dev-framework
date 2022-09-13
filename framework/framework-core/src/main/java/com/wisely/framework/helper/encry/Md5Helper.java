package com.wisely.framework.helper.encry;

import com.wisely.framework.helper.ConfigHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;


/**
 * Md5Helper
 * MD5加密工具类
 */
public class Md5Helper {
    /**
     * 默认的密码字符串组合，apache校验下载的文件的正确性用的就是默认的这个组合
     */
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String SHA = ConfigHelper.getString("spring.md5.signature.sha", "SDJFLSDFSDF");

    /**
     * 适用于上G大的文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String encryptFile(File file) throws IOException {
        try {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] cache = new byte[1048576];
            for (int i = in.read(cache); i != -1; i = in.read(cache)) {
                out.write(cache, 0, i);
            }
            in.close();
            out.close();

            return encryptMD5(out.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String encryptMD5(String s) {
        return encryptMD5(s.getBytes());
    }

    public static String encryptMD5(byte[] bytes) {
        return encrypt(bytes, "MD5");
    }


    public static String encryptSHA(String s) {
        return encryptMD5(s.getBytes());
    }

    public static String encryptSHA(byte[] bytes) {
        return encrypt(bytes, "SHA");
    }

    public static String encrypt(byte[] bytes, String instance) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance(instance);
            messagedigest.update(bytes);
            return bufferToHex(messagedigest.digest());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }


    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * 生成签名
     *
     * @param string
     * @return
     */
    public static String signature(String string) {
        return encryptMD5(string + SHA);
    }


    /**
     * MD5验证
     *
     * @param value
     * @param md5ValueStr
     * @return
     */
    public static boolean checkMD5(String value, String md5ValueStr) {
        return encryptMD5(value).equals(md5ValueStr);
    }


    /**
     * SHA验证
     *
     * @param value
     * @param shaValueStr
     * @return
     */
    public static boolean checkSHA(String value, String shaValueStr) {
        return encryptSHA(value).equals(shaValueStr);
    }


    private final static String PASSWORD_KEY = "WiselyMan";

    /**
     * 密码加密
     *
     * @param account  账号
     * @param password 密码
     * @param salt     盐
     * @return
     */
    public static String encryptPwd(String account, String password, String salt) {
        return encryptMD5(account + password + PASSWORD_KEY + salt);
    }

}
