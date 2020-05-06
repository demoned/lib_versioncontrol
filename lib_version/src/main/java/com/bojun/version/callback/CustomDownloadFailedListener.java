package com.bojun.version.callback;

import android.app.Dialog;
import android.content.Context;

import com.bojun.version.builder.UIData;

public interface CustomDownloadFailedListener {
    Dialog getCustomDownloadFailed(Context context, UIData versionBundle);
}
