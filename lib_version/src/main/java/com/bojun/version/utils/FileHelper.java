package com.bojun.version.utils;

import android.os.Environment;

import java.io.File;

public class FileHelper {

    public static String getDownloadApkCachePath() {
        String appCachePath = null;
        if (checkSDCard()) {
            appCachePath = Environment.getExternalStorageDirectory() + "/AllenVersionPath/";
        } else {
            appCachePath = Environment.getDataDirectory().getPath() + "/AllenVersionPath/";
        }
        File file = new File(appCachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return appCachePath;
    }

    /**
     * 检查sd 卡是否存在
     */
    public static boolean checkSDCard() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        return sdCardExist;
    }
}