package com.zh.core;

import com.sun.org.apache.bcel.internal.generic.BIPUSH;

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
//        return a.gcd(b);
        // 使用递归形式的欧几里得算法
        // 如果b=0,返回
        if (b.compareTo(BigInteger.ZERO) == 0) {
            return a;
        }
        // 如果b!=0,递归
        else {
            return gcd(b, a.mod(b));
        }
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
//            while (x.compareTo(BigInteger.ZERO) < 0) {
//                x = x.add(n);
//                y = y.subtract(a);
//            }
//            return x;
            // ((x % n) + n) % n, 通过取模解决x为负数问题
            return x.mod(n).add(n).mod(n);
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
     * byte[]转BigInteger,无符号
     * 现在byte[]高位加一个字节0,再转换成BigInteger
     * @param a
     * @return
     */
    public static BigInteger byteArrayToBigInteger(byte[] a) {
        byte[] b = new byte[a.length + 1];
        b[0] = 0;
        System.arraycopy(a, 0, b, 1, a.length);
        return new BigInteger(b);
    }

    /**
     * BigInteger转指定长度的byte[]
     * @param bigInteger
     * @return
     */
    public static byte[] bigIntegerToByteArray(BigInteger bigInteger, int length) {
        byte[] b = bigInteger.toByteArray();
        byte[] a = new byte[length];
        System.arraycopy(b, 0, a, a.length-b.length, b.length);
        return a;
    }

    /**
     * 将byte[]转换成指定长度
     * 如果a.length > length,只保留低位
     * 如果a.length < length,高位填充0
     * @param a
     * @param length
     * @return
     */
    public static byte[] transformByteArray(byte[] a, int length) {
        if (a.length == length) {
            return a;
        } else if (a.length > length) {
            byte[] b = new byte[length];
            System.arraycopy(a, a.length-b.length, b, 0, b.length);
            return b;
        } else {
            byte[] b = new byte[length];
            System.arraycopy(a, 0, b, length-a.length, a.length);
            return b;
        }
    }

    /**
     * 对byte[]分组,分组长度必须大于等于4字节
     * 将length个byte转换成为1个BigInteger
     * 如果最后剩余的byte个数小于length,使用0进行填充
     * BigInteger[]的最后一个元素的值表示填充的字节数
     * @param a 待分组的字节数组
     * @param length 分组长度
     * @return BigInteger[]
     */
    public static BigInteger[] doBlock(byte[] a, int length) {
        byte[] b = new byte[length];
        int blockNum;
        int fillLength;
        BigInteger[] result;
        // 数组长度小于分组长度
//        if (a.length < length) {
//            blockNum = 2;
//            fillLength = length - a.length;
//            result = new BigInteger[blockNum];
//            System.arraycopy(a,0,b,0,a.length);
//            for (int i = a.length; i < length; i++) {
//                b[i] = 0;
//            }
//            result[0] = byteArrayToBigInteger(b);
//            b = intToByteArray(fillLength, length);
//            result[1] = byteArrayToBigInteger(b);
//        }
        // 分组长度整除数组长度
        if (a.length % length == 0) {
            blockNum = a.length / length + 1;
            fillLength = 0;
            result = new BigInteger[blockNum];
            for (int i = 0; i < blockNum - 1; i++) {
                System.arraycopy(a, i*length, b, 0, length);
                result[i] = byteArrayToBigInteger(b);
            }
            b = intToByteArray(fillLength, length);
            result[blockNum-1] = byteArrayToBigInteger(b);
        }
        // 分组长度不整除数组长度
        else {
            blockNum = a.length / length + 2;
            fillLength = length - a.length % length;
            result = new BigInteger[blockNum];
            for (int i = 0; i < blockNum - 2; i++) {
                System.arraycopy(a, i*length, b, 0, length);
                result[i] = byteArrayToBigInteger(b);
            }
            System.arraycopy(a, (blockNum-2)*length, b, 0, a.length % length);
            for (int i = a.length % length; i < length; i++) {
                b[i] = 0;
            }
            result[blockNum-2] = byteArrayToBigInteger(b);
            b = intToByteArray(fillLength, length);
            result[blockNum-1] = byteArrayToBigInteger(b);
        }
        return result;

    }

    /**
     * byte[]转int,若byte[]长度大于4,只使用后4个字节
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        //只取后四个字节
        int result = 0;
        if (bytes.length == 1) {
            result = result << 8 | bytes[bytes.length-1] & 0xff;
        } else if (bytes.length == 2) {
            result = result << 8 | bytes[bytes.length-2] & 0xff;
            result = result << 8 | bytes[bytes.length-1] & 0xff;
        } else if (bytes.length == 3) {
            result = result << 8 | bytes[bytes.length-3] & 0xff;
            result = result << 8 | bytes[bytes.length-2] & 0xff;
            result = result << 8 | bytes[bytes.length-1] & 0xff;
        } else {
            //将每个byte依次搬运到int相应的位置
            result = bytes[bytes.length-4] & 0xff;
            result = result << 8 | bytes[bytes.length-3] & 0xff;
            result = result << 8 | bytes[bytes.length-2] & 0xff;
            result = result << 8 | bytes[bytes.length-1] & 0xff;
        }

        return result;
    }

    /**
     * int转byte[],若length大于四,int值放在后四个字节,使用0填充前面的字节
     * @param num
     * @param length 指定返回byte长度
     * @return
     */
    public static byte[] intToByteArray(int num, int length) {
        byte[] bytes = new byte[length];
        //通过移位运算，截取低8位的方式，将int保存到byte数组后4个字节
        bytes[length-4] = (byte)(num >>> 24);
        bytes[length-3] = (byte)(num >>> 16);
        bytes[length-2] = (byte)(num >>> 8);
        bytes[length-1] = (byte)num;
        for (int i = 0; i < length - 4; i++) {
            bytes[i] = (byte)0;
        }
        return bytes;
    }

    /**
     * 加密byte[]
     * @param bytes
     * @param e
     * @param n
     * @return
     */
    public static byte[] encryptByteArray(byte[] bytes, BigInteger e, BigInteger n) {
        // 分组的字节数
        // 根据n的大小自动生成分组字节数
        int length = n.bitLength() / 8 - 1;
        // 每个BigInteger转换成字节个数
        // 加密后得到的BigInteger转输出byte[]使用
        int bigIntegerToArrayLength = n.bitLength()/8 + 1;
        // 将byte[]分组转换成BigInteger[]
        BigInteger[] bigIntegers = doBlock(bytes, length);
        // 加密后的BigInteger[]
        BigInteger[] bigIntegersE = new BigInteger[bigIntegers.length];
        // 暂存加密后的BirInteger转换成的byte[]
        byte[] a;
        byte[] result = new byte[bigIntegers.length * bigIntegerToArrayLength];
        // 循环加密
        for (int i = 0; i < bigIntegers.length; i++) {
            bigIntegersE[i] = encrypt(bigIntegers[i], e, n);
            a = bigIntegerToByteArray(bigIntegersE[i], bigIntegerToArrayLength);
            System.arraycopy(a, 0, result, i*bigIntegerToArrayLength, a.length);
        }
        return result;
    }

    /**
     * 解密byte[]
     * @param bytes
     * @param d
     * @param n
     * @return
     */
    public static byte[] decryptByteArray(byte[] bytes, BigInteger d, BigInteger n) {
        // 分组的字节数
        // 根据n的大小自动生成分组字节数
        int blockNum = n.bitLength() / 8 - 1;
        // bigIntegerToArrayLength个字节转换成1个BigInteger
        // 输入的byte[]转BigInteger时使用
        int bigIntegerToArrayLength = n.bitLength()/8 + 1;
        // 解密后的BigInteger[]长度
        int length = bytes.length / bigIntegerToArrayLength;
        // 解密后的BigInteger[]
        BigInteger[] bigIntegersD = new BigInteger[length];
        // 暂存每次读取的bigIntegerToArrayLength个字节
        byte[] a = new byte[bigIntegerToArrayLength];
        for (int i = 0; i < length ; i++) {
            System.arraycopy(bytes, i*bigIntegerToArrayLength, a, 0, bigIntegerToArrayLength);
            bigIntegersD[i] = decrypt(byteArrayToBigInteger(a), d, n);
        }
        int fillNum = bigIntegersD[bigIntegersD.length-1].intValue();
        if (fillNum == 0) {
            // 用于返回的byte[]
            byte[] result = new byte[(length-1)*blockNum];
            // 用于暂存BigInteger得到的byte[]
            byte[] b;
            for (int i = 0; i < bigIntegersD.length - 1; i++) {
                b = bigIntegersD[i].toByteArray();
                // 将b转换成指定长度
                b = transformByteArray(b, blockNum);
                System.arraycopy(b, 0, result, i*blockNum, b.length);
            }
            return result;
        } else {
            // 用于返回的byte[]
            byte[] result = new byte[(length-1)*blockNum - fillNum];
            // 用于暂存BigInteger得到的byte[]
            byte[] b;
            for (int i = 0; i < bigIntegersD.length - 2; i++) {
                b = bigIntegersD[i].toByteArray();
                // 将b转换成指定长度
                b = transformByteArray(b, blockNum);
                System.arraycopy(b, 0, result, i*blockNum, b.length);
            }
            b = bigIntegersD[bigIntegersD.length-2].toByteArray();
//            System.out.println(b.length);
            b = transformByteArray(b, blockNum);
//            b = transformByteArray(b, blockNum-fillNum);
            System.arraycopy(b, 0, result, (bigIntegersD.length-2)*blockNum, blockNum-fillNum);
            return result;
        }

    }

}
