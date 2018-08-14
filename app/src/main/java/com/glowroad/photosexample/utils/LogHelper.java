package com.glowroad.photosexample.utils;

import android.util.Log;

import com.glowroad.photosexample.BuildConfig;

/**
 * Created by Mahesh Kumar on 8/14/2018.
 */
public class LogHelper {

    public static String LOG_TAG = "LOG_TAG";

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, msg);
        }

    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(LOG_TAG, msg);
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(LOG_TAG, msg);
        }
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, msg);
        }
    }
}