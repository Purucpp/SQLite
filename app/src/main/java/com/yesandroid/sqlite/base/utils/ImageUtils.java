package com.yesandroid.sqlite.base.utils;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.util.Log;

public class ImageUtils {
    public static double getScale(int width, int height, int desiredWidth, int desiredHeight) {
        double scale;
        if ((width == 0 || desiredWidth == 0) && (height == 0 || desiredHeight == 0)) {
            return 1;
        } else if (width == 0 || desiredWidth == 0) {
            scale = (double) desiredHeight / height;
        } else if (height == 0 || desiredHeight == 0) {
            scale = (double) desiredWidth / width;
        } else {
            scale = Math.min((double) desiredWidth / width, (double) desiredHeight / height);
        }
        return scale;
    }

    public static Bitmap scaleBitmap(Bitmap src, int desiredWidth, int desiredHeight) {
        int width;
        int height;
        try {
            width = src.getWidth();
            height = src.getHeight();
        } catch (Exception e) {
            Log.d(TAG, "scaleBitmap: Exception caught");
            e.printStackTrace();
            return src;
        }

        double scale = getScale(width, height, desiredWidth, desiredHeight);

        if (height == 0 || width == 0 || scale == 0) {
            Log.d(TAG, "scaleBitmap: Height: " + height);
            Log.d(TAG, "scaleBitmap: Width:  " + width);
            Log.d(TAG, "scaleBitmap: Scale:  " + scale);
            return src;
        }

        return Bitmap.createScaledBitmap(src, (int) (width * scale),
                (int) (height * scale), false);
    }


}
