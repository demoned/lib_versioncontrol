package com.bojun.version.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.bojun.version.core.AllenChecker;
import com.bojun.version.core.VersionFileProvider;
import com.bojun.version.AllenVersionChecker;
import com.bojun.version.callback.CustomInstallListener;

import java.io.File;

public final class AppUtils {
    private AppUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = VersionFileProvider.getUriForFile(context, context.getPackageName() + ".versionProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
        AllenChecker.cancelMission();
        AllenVersionChecker.getInstance().cancelAllMission();
    }

    public static void installApk(Context context, File file, CustomInstallListener listener) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = VersionFileProvider.getUriForFile(context, context.getPackageName() + ".versionProvider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        if (listener != null) {
            listener.install(context, uri);
        } else {
            intent.setDataAndType(uri,
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
            AllenChecker.cancelMission();
            AllenVersionChecker.getInstance().cancelAllMission();
        }
    }
}