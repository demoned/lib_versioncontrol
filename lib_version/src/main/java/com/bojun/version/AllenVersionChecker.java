package com.bojun.version;

import android.content.Context;

import androidx.annotation.Nullable;

import com.bojun.version.core.http.AllenHttp;
import com.bojun.version.utils.AllenEventBusUtil;
import com.bojun.version.builder.DownloadBuilder;
import com.bojun.version.builder.RequestVersionBuilder;
import com.bojun.version.builder.UIData;
import com.bojun.version.eventbus.AllenEventType;

public class AllenVersionChecker {

    private AllenVersionChecker() {

    }

    public static AllenVersionChecker getInstance() {
        return AllenVersionCheckerHolder.allenVersionChecker;
    }

    private static class AllenVersionCheckerHolder {
        public static final AllenVersionChecker allenVersionChecker = new AllenVersionChecker();
    }

    @Deprecated
    public void cancelAllMission(Context context) {
        cancelAllMission();
    }

    public void cancelAllMission() {
        AllenHttp.getHttpClient().dispatcher().cancelAll();
        AllenEventBusUtil.sendEventBusStick(AllenEventType.CLOSE);
        AllenEventBusUtil.sendEventBusStick(AllenEventType.STOP_SERVICE);
    }

    /**
     * @param versionBundle developer should return version bundle ,to use when showing UI page,could be null
     * @return download builder for download setting
     */
    public DownloadBuilder downloadOnly(@Nullable UIData versionBundle) {
        return new DownloadBuilder(null, versionBundle);
    }

    /**
     * use request version function
     *
     * @return requestVersionBuilder
     */
    public RequestVersionBuilder requestVersion() {
        return new RequestVersionBuilder();
    }
}
