package com.example.mytcpandws.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * createBy ${huanghao}
 * on 2019/7/8
 */
public class Utils {
    public static int getCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * @param x 字符串
     * @return 字节数组
     */
    public static byte[] string2bytes(String x) {
        try {
            byte[] bs = x.getBytes("utf-8");
            ByteBuffer byteBuffer = ByteBuffer.wrap(bs);
            return byteBuffer.array();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] string2littelbytes(String x) {
        try {
            byte[] bs = x.getBytes("utf-8");
            ByteBuffer byteBuffer = ByteBuffer.wrap(bs);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            return byteBuffer.array();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] int2bytes(int x, ByteOrder order) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(order);
        buffer.putInt(x);
        return buffer.array();
    }

    public static byte[] long2bytes(long x, ByteOrder order) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(order);
        buffer.putLong(x);
        return buffer.array();
    }


    //合并所有的byte数组变成一个
    private static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

}
