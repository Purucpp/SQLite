package com.yesandroid.sqlite.base.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PrimitiveUtils {

    // Follow Big endian
    public static Double byteToDouble(short[] data) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < data.length; i++) {
            String singleByteData = Integer.toHexString(data[i]);
            if (singleByteData.length() == 1) {
                singleByteData = "0" + singleByteData;
            }
            stringBuilder.append(singleByteData);
        }

        double value = Integer.parseInt(stringBuilder.toString(), 16);
        if (value > 8388607) {
            return (16777215.0 - value + 1) * -1;
        } else {
            return value;
        }

    }

    public static double convertByteToDecimal(byte[] bytes, int bits) {
        String hex = byteArrayToHex(bytes);
       // Log.d("byte hex - short hex",byteArrayToHex(shorts)+"--"+convertShortToHex(s));
        int value = Integer.parseInt(hex, 16);
        int sign = value & 1 << (bits - 1);

        if (sign > 0) {
            value = value - (1 << bits);
        }
        return value;
    }

    public static double convertShortToDecimal(short[] shorts, int bits) {
        String hex = convertShortToHex(shorts);
        // Log.d("byte hex - short hex",byteArrayToHex(shorts)+"--"+convertShortToHex(s));
        int value = Integer.parseInt(hex, 16);
        int sign = value & 1 << (bits - 1);

        if (sign > 0) {
            value = value - (1 << bits);
        }
        return value;
    }

    public static String convertShortToHex(short[] bytes) {
        StringBuilder result = new StringBuilder();
        for (short temp : bytes) {
            result.append(String.format("%02x", temp));
        }
        return result.toString();
    }

    public static List<Long> asLongList(long[] data) {

        // if(data instanceof Long[]) Future use possible for all primitive data types
        List<Long> newData = new ArrayList<>();

        for (long partOfData : data) {
            newData.add(partOfData);
        }

        return newData;
    }


    public static long[] toPrimitiveLong(List<Long> data) {
        long[] newData = new long[data.size()];


        for (int i = 0; i < data.size(); i++) {
            newData[i] = data.get(i);
        }
        return newData;

    }

    public static double[] flattenDouble(double[][] data) {
        int sizeCounter = 0;
        for (int i = 0; i < data.length; i++) {
            sizeCounter += data[i].length;
        }
        double[] newData = new double[sizeCounter];
        int counter = 0;

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                newData[counter] = data[i][j];
                counter++;
            }
        }
        return newData;

    }

    public static double[] toPrimitiveDouble(List<Double> data) {
        double[] newData = new double[data.size()];


        for (int i = 0; i < data.size(); i++) {
            newData[i] = data.get(i);
        }
        return newData;

    }

    public static String[] toPrimitiveString(List<String> data) {
        String[] newData = new String[data.size()];


        for (int i = 0; i < data.size(); i++) {
            newData[i] = data.get(i);
        }
        return newData;

    }


    public static byte[] covertShortToByte(short[] input) {
        int index;
        int iterations = input.length;

        ByteBuffer bb = ByteBuffer.allocate(input.length * 2);

        for (index = 0; index != iterations; ++index) {
            bb.putShort(input[index]);
        }

        return bb.array();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static long bedbytesToLong(byte[] bytes) {

        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value += ((long) bytes[i] & 0xffL) << (8 * i);
        }
        return value;
    }

    public static long ledBytesToLong(byte[] bytes) {
        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value = (value << 8) + (bytes[i] & 0xff);
        }
        return value;
    }

    public static String bytesToString(byte[] data) {
        return new String(data);

    }

    public static String shortTostring(short[] dataTosend) {
        StringBuilder stringBuilder = new StringBuilder();
        for (short value : dataTosend) {
            stringBuilder.append((char) value);
        }

        return stringBuilder.toString();
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);

        for(byte b: a) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
