package com.bojun.version.utils;

import android.util.Log;

import com.bojun.version.core.AllenChecker;
public class ALog {
    public static void e(String msg) {
        if (AllenChecker.isDebug()) {
            if (msg != null && !msg.isEmpty())
                Log.e("Allen Checker", msg);
        }
    }
}
