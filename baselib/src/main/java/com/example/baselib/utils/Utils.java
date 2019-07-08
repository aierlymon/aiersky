package com.example.baselib.utils;


import android.content.Context;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Davia.Li on 2017-07-31.
 */

public class Utils {



    public static float[] getScreenWH(Context context) {
        float[] ints = new float[2];
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int heigth = dm.heightPixels;
        int width = dm.widthPixels;
        ints[0] = width;
        ints[1] = heigth;
        return ints;
    }

    public static String byteArrayToHexString(byte[] byteBuffer) {
        return byteArrayToHexString(byteBuffer, byteBuffer.length);
    }

    public static String byteArrayToHexString(byte[] byteBuffer, int size) {
        StringBuilder strHexString = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(0xff & byteBuffer[i]);
            if (hex.length() == 1) {
                strHexString.append('0');
            }
            strHexString.append(hex);
        }

        return strHexString.toString();
    }



    //获得不重复的6位密码
    public static String unRepeatSixCode() {
        String sixChar = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        String time = sdf.format(date);
        for (int i = 0; i < time.length() / 2; i++) {
            String singleChar;
            String x = time.substring(i * 2, (i + 1) * 2);
            int b = Integer.parseInt(x);
            if (b < 10) {
                singleChar = Integer.toHexString(Integer.parseInt(x));
            } else if (b >= 10 && b < 36) {
                singleChar = String.valueOf((char) (Integer.parseInt(x) + 55));
            } else {
                singleChar = String.valueOf((char) (Integer.parseInt(x) + 61));
            }
            sixChar = sixChar + singleChar;

        }
        System.out.println("生成一个6位不可重复的字符编码是：" + sixChar);
        return sixChar;
    }

    //获得不重复的8位密码
    public static String unRepeatEightCode() {
        String sixChar = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSSS");
        Date date = new Date();
        String time = sdf.format(date);
        for (int i = 0; i < time.length() / 2; i++) {
            String singleChar;
            String x = time.substring(i * 2, (i + 1) * 2);
            int b = Integer.parseInt(x);
            if (b < 10) {
                singleChar = Integer.toHexString(Integer.parseInt(x));
            } else if (b >= 10 && b < 36) {
                singleChar = String.valueOf((char) (Integer.parseInt(x) + 55));
                //A--Z
            } else if ((b > 61&&b<=64)) {
                singleChar = String.valueOf((char) (Integer.parseInt(x) + 20));
            } else if ((b >64&&b<90)) {
                singleChar = String.valueOf((char) (Integer.parseInt(x)));
            } else if ((b >=90)) {
                singleChar = String.valueOf((char) ((Integer.parseInt(x)-10)));
            } else {
                //18年
                singleChar = String.valueOf((char) (Integer.parseInt(x) + 61));
            }
            sixChar = sixChar + singleChar;

        }
        return sixChar;
    }


}
