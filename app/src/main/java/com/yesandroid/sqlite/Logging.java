package com.yesandroid.sqlite;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logging {

    public static void appendLog(String text, String folder)
    {
        Log.d("app->","app");
        File logFile = new File(Environment.getExternalStorageDirectory(),folder+"_Hex.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();

            Log.d("loggin-->","app");
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();

            Log.d("loggin failed-->","app"+e);
        }
    }


    public void decLog(String text, String folder)
    {
        Log.d("app->","app");
        File logFile = new File(Environment.getExternalStorageDirectory(),folder+"_Dec.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();

            Log.d("loggin-->","app");
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();

            Log.d("loggin failed-->","app"+e);
        }
    }

}
