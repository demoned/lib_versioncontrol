package com.bojun.version.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.bojun.version.AllenVersionChecker;
import com.bojun.version.R;
import com.bojun.version.builder.DownloadBuilder;
import com.bojun.version.builder.UIData;
import com.bojun.version.eventbus.AllenEventType;
import com.bojun.version.utils.ALog;
import com.bojun.version.utils.AllenEventBusUtil;
import com.bojun.version.utils.AppUtils;

import java.io.File;

public class UIActivity extends AllenBaseActivity implements DialogInterface.OnCancelListener {
    private Dialog versionDialog;
    private boolean isDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.e("version activity create");
        showVersionDialog();
    }

    @Override
    protected void onDestroy() {
        isDestroy = true;
        ALog.e("version activity destroy");
        super.onDestroy();
    }

    @Override
    public void showDefaultDialog() {
        if (getVersionBuilder() != null) {
            UIData uiData = getVersionBuilder().getVersionBundle();
            String title = "提示", content = "检测到新版本";
            if (uiData != null) {
                title = uiData.getTitle();
                content = uiData.getContent();
            }
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this).setTitle(title).setMessage(content).setPositiveButton(getString(R.string.lib_confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dealVersionDialogCommit();

                }
            });
            if (getVersionBuilder().getForceUpdateListener() == null) {
                alertBuilder.setNegativeButton(getString(R.string.lib_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onCancel(versionDialog);
                    }
                });
                alertBuilder.setCancelable(false);


            } else {
                alertBuilder.setCancelable(false);
            }
            versionDialog = alertBuilder.create();
            versionDialog.setCanceledOnTouchOutside(false);
            versionDialog.show();
        }
    }

    @Override
    public void showCustomDialog() {
        if (getVersionBuilder() != null) {
            ALog.e("show customization dialog");
            versionDialog = getVersionBuilder().getCustomVersionDialogListener().getCustomVersionDialog(this, getVersionBuilder().getVersionBundle());
            try {
                //自定义dialog，commit button 必须存在
                final View view = versionDialog.findViewById(R.id.lib_version_dialog_commit);
                if (view != null) {
                    ALog.e("view not null");

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ALog.e("click");
                            dealVersionDialogCommit();
                        }
                    });

                } else {
                    throwWrongIdsException();
                }
                //如果有取消按钮，id也必须对应
                View cancelView = versionDialog.findViewById(R.id.lib_version_dialog_cancel);
                if (cancelView != null) {
                    cancelView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onCancel(versionDialog);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                throwWrongIdsException();
            }
            versionDialog.show();
        }
    }

    private void showVersionDialog() {
        if (getVersionBuilder() != null && getVersionBuilder().getCustomVersionDialogListener() != null) {
            showCustomDialog();
        } else {
            showDefaultDialog();
        }
        if (versionDialog != null) {
            versionDialog.setOnCancelListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (versionDialog != null && versionDialog.isShowing())
            versionDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (versionDialog != null && !versionDialog.isShowing())
            versionDialog.show();
    }

    private void dealVersionDialogCommit() {
        DownloadBuilder versionBuilder = getVersionBuilder();
        if (versionBuilder != null) {
            //增加commit 回调
            if (versionBuilder.getReadyDownloadCommitClickListener() != null) {
                versionBuilder.getReadyDownloadCommitClickListener().onCommitClick();
            }
            //如果是静默下载直接安装
            if (versionBuilder.isSilentDownload()) {
                String downloadPath = versionBuilder.getDownloadAPKPath() + getString(R.string.lib_download_apkname, versionBuilder.getApkName() != null ? versionBuilder.getApkName() : getPackageName());
                AppUtils.installApk(this, new File(downloadPath), versionBuilder.getCustomInstallListener());
                checkForceUpdate();
                //否定开始下载
            } else {
                AllenEventBusUtil.sendEventBus(AllenEventType.START_DOWNLOAD_APK);
            }
            finish();
        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        cancelHandler();
        checkForceUpdate();
        AllenVersionChecker.getInstance().cancelAllMission();
        finish();
    }
}
