package com.yesandroid.sqlite.base.constants;

public class Constants {

    @SuppressWarnings("HardCodedStringLiteral")
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    @SuppressWarnings("HardCodedStringLiteral")
    public static final String DEFAULT_TIME_FORMAT = "kk:mm:ss";
    public static final String DEFAULT_DATE_TIME_FORMAT = DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT;

    /**
     * SAFE_THREAD_MIN_MEM                  Minimum memory required (in bytes) for executing new thread
     * SAFE_THREAD_FORCE_CLEAN_THRESHOLD    Threshold value, after which manual GC is called
     * SAFE_THREAD_SLEEP_MILL               Time in mills required to wait for unsafe threads before
     *                                      checking safe ram usage values
     */
    public static final long SAFE_THREAD_MIN_MEM = 50 * 1024 * 1024; // 50mb
    public static final long SAFE_THREAD_FORCE_CLEAN_THRESHOLD = 25 * 1024 * 1024; // 25mb
    public static final long SAFE_THREAD_SLEEP_MILL = 100;

}