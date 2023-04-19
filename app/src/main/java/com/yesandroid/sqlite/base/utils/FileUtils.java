package com.yesandroid.sqlite.base.utils;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;

public class FileUtils {


    @Nullable
    public static File createFile(String s) throws IOException {
        File localFile = new File(s);
        if (localFile.exists()) {
            if (localFile.delete())
                return createFile(s);
            else
                return null;
        } else {
            new File(localFile.getParent()).mkdirs();
            if (localFile.createNewFile()) {
                return localFile;
            } else {
                return null;
            }
        }
    }


}