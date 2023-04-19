package com.yesandroid.sqlite.base.utils;

public class TextUtils {


    public static String upperCaseAllFirst(String value) {
        if (value == null || value.trim().equals("")) {
            return "";
        }
        char[] array = value.toCharArray();
        // Uppercase first letter.
        array[0] = Character.toUpperCase(array[0]);
        // Uppercase all letters that follow a whitespace character.
        for (int i = 1; i < array.length; i++) {
            if (Character.isWhitespace(array[i - 1])) {
                array[i] = Character.toUpperCase(array[i]);
            }
        }
        // Result.
        return new String(array);
    }
}
