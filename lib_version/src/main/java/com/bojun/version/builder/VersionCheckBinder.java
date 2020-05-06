package com.bojun.version.builder;

import android.content.ServiceConnection;
import android.os.Binder;

import com.bojun.version.ui.VersionService;
public class VersionCheckBinder extends Binder {
    private VersionService versionService;
    private ServiceConnection serviceConnection;

    public VersionCheckBinder(VersionService versionService) {
        this.versionService = versionService;
    }

    public VersionCheckBinder setServiceConnection(ServiceConnection serviceConnection) {
        this.serviceConnection = serviceConnection;
        return this;
    }

    public ServiceConnection getServiceConnection() {
        return serviceConnection;
    }

    public VersionCheckBinder setDownloadBuilder(DownloadBuilder downloadBuilder) {
        versionService.setBuilder(downloadBuilder);
        versionService.init();
        return this;
    }
}
