package com.yesandroid.sqlite.base.utils;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.HashMap;

public class Cache {

    private static HashMap<String, Bitmap> cache = null;

    public static void addBitmapToCache(String key, Bitmap bmp) {
        if (cache == null) {
            Log.d(TAG, "addBitmapToCache: created new cache");
            cache = new HashMap<>(8);
        }
        try {
            cache.put(key, bmp);
            Log.d(TAG, "addBitmapToCache: added: " + key);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "addBitmapToCache: failed to add: " + key);
        }
    }

    public static Bitmap getBitmapFromCache(String key) {
        if (cache == null) {
            cache = new HashMap<>(8);
            return null;
        }
        try {
            return cache.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getBitmapFromCache: Failed ot get file: " + key);
            return null;
        }
    }
}
