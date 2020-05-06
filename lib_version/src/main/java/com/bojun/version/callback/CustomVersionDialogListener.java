package com.bojun.version.callback;

import android.app.Dialog;
import android.content.Context;

import com.bojun.version.builder.UIData;

public interface CustomVersionDialogListener {
    Dialog getCustomVersionDialog(Context context, UIData versionBundle);
}
