package com.bojun.version.callback;

import androidx.annotation.Nullable;

import com.bojun.version.builder.DownloadBuilder;
import com.bojun.version.builder.UIData;

public interface RequestVersionListener {
    /**
     * @param result the result string of request
     * @return developer should return version bundle ,to use when showing UI page,could be null
     */
    @Nullable
    UIData onRequestVersionSuccess(DownloadBuilder downloadBuilder, String result);

    void onRequestVersionFailure(String message);
}
