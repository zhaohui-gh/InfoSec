package com.zh.core;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Scanner;

/**
 * Created by zhaohui on 2019/10/25
 */
public class RSATest {

    public static void main(String[] args) {

        int bitLength = 128;
        BigInteger[] result;
        result = RSA.generateKeys(bitLength);
        BigInteger p = result[0];
        BigInteger q = result[1];
        BigInteger n = result[2];
        BigInteger phi_n = result[3];
        BigInteger e = result[4];
        BigInteger d = result[5];
        System.out.println("请输入原始消息");
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        try {
            byte[] bytes = str.getBytes("utf-8");
            System.out.println("原始消息: " + str);
            byte[] bytesE = RSA.encryptByteArray(bytes, e, n);
            // 使用base64编码输出密文
            String strE = Base64.getEncoder().encodeToString(bytesE);
            System.out.println("密文为: " + strE);
            byte[] bytesD = RSA.decryptByteArray(bytesE, d, n);
            System.out.println("解密后为: " + new String(bytesD, "utf-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

    }

}
