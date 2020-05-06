package com.bojun.version.callback;

import android.app.Dialog;
import android.content.Context;

import com.bojun.version.builder.UIData;

public interface CustomDownloadingDialogListener {
    Dialog getCustomDownloadingDialog(Context context, int progress, UIData versionBundle);

    void updateUI(Dialog dialog, int progress, UIData versionBundle);
}
