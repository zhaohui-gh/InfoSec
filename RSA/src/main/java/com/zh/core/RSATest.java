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
        // RSA.quickModPower测试
//        BigInteger a = new BigInteger("7");
//        BigInteger b = new BigInteger("21");
//        BigInteger c = new BigInteger("23");
//        BigInteger result2;
//        result2 = RSA.quickModPower(a, b, c);
//        System.out.println(result2);
        // RSA.gcd测试
//        BigInteger a = new BigInteger("78");
//        BigInteger b = new BigInteger("118");
//        BigInteger[] result = RSA.extendGcd(a, b);
//        for (int i = 0; i < result.length; i++) {
//            System.out.println(result[i]);
//        }
        // RSA.getInv测试
//        BigInteger a = new BigInteger("7");
//        BigInteger n = new BigInteger("160");
//        BigInteger result = RSA.getInv(a, n);
//        System.out.println(result);
        // RSA.generatePrime测试
//        System.out.println(RSA.generatePrime(128));
        // RSA.generateKeys测试

        int bitLength = 128;
        BigInteger[] result;
        result = RSA.generateKeys(bitLength);
        BigInteger p = result[0];
        BigInteger q = result[1];
        BigInteger n = result[2];
        BigInteger phi_n = result[3];
        BigInteger e = result[4];
        BigInteger d = result[5];
        Integer mInt = 12345;
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
//        BigInteger mBigInt = new BigInteger(mInt.toString());
//        System.out.println("原始消息: " + mBigInt.toString());
//        BigInteger eBigInt = RSA.encrypt(mBigInt, e, n);
//        System.out.println("密文: " + eBigInt.toString());
//        BigInteger dBigInt = RSA.decrypt(eBigInt, d, n);
//        System.out.println("解密后: " + dBigInt.toString());

//        System.out.println("请输入原始消息");
//        Scanner in = new Scanner(System.in);
//        String testString = in.nextLine();
//        BigInteger bigInteger = new BigInteger(testString.getBytes());
//        System.out.println(bigInteger.abs().toString());
//
//        System.out.println(bigInteger.toString());
//        BigInteger bigInteger1 = new BigInteger(1, testString.getBytes());
//        System.out.println(bigInteger1.toString());
//        String string = new String(bigInteger.toByteArray());
//        System.out.println(string);
//        String string1 = new String(bigInteger1.abs().toByteArray());
//        System.out.println(string1);
//        try {
//            byte[] bytes1 = new byte[4];  //用于加密的数组
//
//            byte[] bytes = testString.getBytes("utf-8");  //字符串转化的字节流
//            byte[] bytesPad = new byte[bytes.length * 4];  //字符串转化的字节流
//            int[] ints = new int[bytes.length];
//            BigInteger[] eMeg = new BigInteger[bytes.length];
//            BigInteger[] dMeg = new BigInteger[bytes.length];
//            System.out.println("原始消息: " + testString);
//            System.out.print("加密后为: ");
//            for (int i = 0; i < bytes.length; i++) {
//                System.arraycopy(bytes, i, bytes1, 3, 1);
//                ints[i] = RSA.byteArrayToInt(bytes1);
//                eMeg[i] = new BigInteger(((Integer)ints[i]).toString());
//                eMeg[i] = RSA.encrypt(eMeg[i], e, n);
//                System.out.print(Integer.toHexString(eMeg[i].intValue()));
//            }
//            System.out.println();
//            for (int i = 0; i < eMeg.length; i++) {
//                dMeg[i] = RSA.decrypt(eMeg[i], d, n);
//                ints[i] = dMeg[i].intValue();
//                bytes1 = RSA.intToByteArray(ints[i], 4);
//                System.arraycopy(bytes1, 3, bytes, i, 1);
//            }
//            String decrypyString = new String(bytes, "utf-8");
//            System.out.println("解密后为: " + decrypyString);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

//        String testString = "￥￥￥这是测试文本hahaha！^-^这是测试文本hahaha！^-^这是测试文本hahaha！^-^这是测试文本hahaha！^-^";
//        System.out.println(testString);
//        try {
//            byte[] bytes = testString.getBytes("utf-8");
////            BigInteger bigInteger = RSA.byteArrayToBigInteger(bytes);
////            System.out.println(bigInteger.toString());
////
////            bytes = RSA.bigIntegerToByteArray(bigInteger);
//            // 分组的字节数
////            int length = Math.max(n.bitLength() / 8 - 1, 4);
//            int length = 128;
//            // 将byte[]分组转换成BigInteger[]
//            BigInteger[] bigIntegers = RSA.doBlock(bytes, length);
//            // 加密后的BigInteger[]
//            BigInteger[] bigIntegersE = new BigInteger[bigIntegers.length];
//            // 解密后的BigInteger[]
//            BigInteger[] bigIntegersD = new BigInteger[bigIntegers.length];
//            for (int i = 0; i < bigIntegers.length; i++) {
//                // 对每个BigInteger加密
//                bigIntegersE[i] = RSA.encrypt(bigIntegers[i], e, n);
//                bigIntegersD[i] = RSA.decrypt(bigIntegersE[i], d, n);
//                System.out.println("***********");
//                System.out.println(i);
//                System.out.println(bigIntegers[i].toString());
//                System.out.println(bigIntegersE[i].toString());
//                System.out.println(bigIntegersD[i].toString());
//                System.out.println("***********");
//            }
//            System.out.println("---------------------------------");
//            bytes = bigIntegers[bigIntegers.length-1].toByteArray();
//            System.out.println(RSA.byteArrayToInt(bytes));
//            System.out.println("---------------------------------");
//            System.out.println(testString);
//            bytes = testString.getBytes("utf-8");
//            System.out.println(bytes.length);
//            byte[] x = RSA.encryptByteArray(bytes, e, n);
//            System.out.println(x.length);
//            x = RSA.decryptByteArray(x, d, n);
//            System.out.println(x.length);
//            System.out.println(new String(x, "utf-8"));
//        } catch (UnsupportedEncodingException ex) {
//            ex.printStackTrace();
//        }

    }

}
