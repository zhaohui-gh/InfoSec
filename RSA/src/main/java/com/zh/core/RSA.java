package com.zh.core;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by zhaohui on 2019/10/25
 */
public class RSA {

    /**
     * 快速模指数法，计算a^b(mod c)的值
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static BigInteger quickModPower(BigInteger a, BigInteger b, BigInteger c) {
        BigInteger result = new BigInteger("1");
        a = a.mod(c);
        while (b.intValue() != 0) {
            // 如果b是奇数
            if (b.mod(new BigInteger("2")).intValue() == 1) {
                result = (result.multiply(a)).mod(c);
            }
            a = a.multiply(a).mod(c);
            b = b.divide(new BigInteger("2"));
        }
        return result;
    }

    /**
     * 求a和b的最大公因数
     * @param a
     * @param b
     * @return
     */
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        return a.gcd(b);
    }

    /**
     * 扩展的欧几里得算法 ax+by=gcd(a,b),返回x,y,gcd(a,b)
     * @param a
     * @param b
     * @return
     */
    public static BigInteger[] extendGcd(BigInteger a, BigInteger b) {
        BigInteger[] result = new BigInteger[3];
        BigInteger x;
        BigInteger y;
        BigInteger d;
        BigInteger t;
        if (b.intValue() == 0) {
            result[0] = new BigInteger("1");
            result[1] = new BigInteger("0");
            result[2] = a;
            return result;
        } else {
            result = extendGcd(b, a.mod(b));
            x = result[0];
            y = result[1];
            d = result[2];
            t = x;
            x = y;
            y = t.subtract(a.divide(b).multiply(y));
            result[0] = x;
            result[1] = y;
            result[2] = d;
            return result;
        }
    }

    /**
     * 求乘法逆元 a*x mod n = 1,求x
     * @param a
     * @param n
     * @return
     */
    public static BigInteger getInv(BigInteger a, BigInteger n){
        BigInteger[] result = new BigInteger[3];
        BigInteger x;
        BigInteger y;
        BigInteger d;
        result = extendGcd(a, n);
        x = result[0];
        y = result[1];
        d = result[2];
        if (d.intValue() == 1) {
            // ax+by=1 x必须大于0
            while (x.compareTo(BigInteger.ZERO) < 0) {
                x = x.add(n);
                y = y.subtract(a);
            }
//            System.out.println("a = " + a.toString());
//            System.out.println("x = " + x.toString());
//            System.out.println("n = " + n.toString());
//            System.out.println("y = " + y.toString());
//            System.out.println("gcd(a, n) = " + gcd(a, n).toString());
//            System.out.println("ax + ny = " + a.multiply(x).add(n.multiply(y)));
            return x;
        } else {
            return new BigInteger("0");
        }
    }

    /**
     * 产生指定二进制位数的大素数
     * @param bitLength
     * @return
     */
    public static BigInteger generatePrime(int bitLength) {
        return BigInteger.probablePrime(bitLength, new Random());
    }

    /**
     * 随机产生与小于n且与n互素的数
     * @param n
     * @return
     */
    public static BigInteger generatePrimeLessN(BigInteger n) {
        Random random = new Random();
        Integer randomInt = random.nextInt(100);
        BigInteger m = n.divide(new BigInteger(randomInt.toString()));
        BigInteger one = new BigInteger("1");
        while (gcd(n, m).intValue() != 1) {
            m = m.add(one);
        }
        return m;
    }

    /**
     * 产生密钥
     * @param bitLength
     * @return
     */
    public static BigInteger[] generateKeys(int bitLength) {
        BigInteger[] result = new BigInteger[6];
        BigInteger p;
        BigInteger q;
        BigInteger n;
        BigInteger phi_n;
        BigInteger e;
        BigInteger d;
        p = generatePrime(bitLength + 1);
        q = generatePrime(bitLength - 1);
        n = p.multiply(q);
        phi_n = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
        e = generatePrimeLessN(phi_n);
        d = getInv(e, phi_n);
        result[0] = p;
        result[1] = q;
        result[2] = n;
        result[3] = phi_n;
        result[4] = e;
        result[5] = d;
        System.out.println("p = " + p.toString());
        System.out.println("q = " + q.toString());
        System.out.println("n = " + n.toString());
        System.out.println("phi_n = " + phi_n.toString());
        System.out.println("e = " + e.toString());
        System.out.println("d = " + d.toString());
        return result;
    }

    /**
     * 加密 m^e mod n
     * @param m
     * @param e
     * @param n
     * @return
     */
    public static BigInteger encrypt(BigInteger m, BigInteger e, BigInteger n) {
        if (m.compareTo(n) > 0) {
            return null;
        }
//        System.out.println("m = " + m.toString());
//        System.out.println("e = " + e.toString());
//        System.out.println("n = " + n.toString());
        return quickModPower(m, e, n);
//        return m.modPow(e, n);
    }

    /**
     * 解密 c^d mod n
     * @param c
     * @param d
     * @param n
     * @return
     */
    public static BigInteger decrypt(BigInteger c, BigInteger d, BigInteger n) {
        return quickModPower(c, d, n);
//        return c.modPow(d, n);
    }

    /**
     * int(2字节)转byte[3] 由高位到低位
     * @param i
     * @return
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        for(int i = 0; i < 4; i++) {
            int shift = (3-i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

}
