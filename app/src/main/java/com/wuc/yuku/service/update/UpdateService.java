package com.wuc.yuku.service.update;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.wuc.yuku.R;
import com.wuc.yuku.activity.InstallPermissionActivity;
import com.wuc.yuku.util.NotificationUtils;

import java.io.File;

/**
 * @author: wuchao
 * @date: 2018/12/27 14:40
 * @desciption: 应用更新组件入口，用来启动下载器并更新Notification
 */
public class UpdateService extends Service {
    /**
     * apk下载地址
     */
    private static final String APK_URL_TITLE = "http://www.imooc.com/mobile/mukewang.apk";
    public static final String APK_URL="apk_url";
    /**
     * 文件存放路径
     */
    private String filePath;
    /**
     * 文件下载地址
     */
    private String apkUrl;

    private NotificationUtils mNotificationUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        filePath = Environment.getExternalStorageDirectory() + "/videobusiness/videobusiness.apk";
        mNotificationUtils = new NotificationUtils(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        apkUrl = APK_URL_TITLE;
        notifyUser(getString(R.string.update_download_start), 0);
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void notifyUser(String content, int progress) {
        mNotificationUtils.sendNotificationProgress(getString(R.string.app_name), content, progress, progress >= 100 ? getContentIntent() : PendingIntent.getActivity(this, 0,
                new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
    }

    private void startDownload() {
        UpdateManager.getInstance().startDownload(apkUrl, filePath, new UpdateDownloadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onPrepared(long contentLength, String downloadUrl) {

            }

            @Override
            public void onProgressChanged(int progress, String downloadUrl) {
                notifyUser(getString(R.string.update_download_processing), progress);
            }

            @Override
            public void onPaused(int progress, int completeSize, String downloadUrl) {
                notifyUser(getString(R.string.update_download_failed), progress);
                deleteApkFile();
                //停止服务自身
                stopSelf();
            }

            @Override
            public void onFinished(int completeSize, String downloadUrl) {
                notifyUser(getString(R.string.update_download_finish), 100);
                //停止服务自身
                stopSelf();
                startActivity(getInstallApkIntent());
            }

            @Override
            public void onFailure() {
                notifyUser(getString(R.string.update_download_failed), 0);
                deleteApkFile();
                //停止服务自身
                stopSelf();
            }
        });
    }

    private PendingIntent getContentIntent() {
        return PendingIntent.getActivity(this, 0,
                getInstallApkIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent getInstallApkIntent() {
        File apkFile = new File(filePath);
        // 通过Intent安装APK文件
        final Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.addCategory(Intent.CATEGORY_DEFAULT);
        //兼容7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", apkFile);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            //兼容8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = this.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    InstallPermissionActivity.sListener = new UpdateManager.InstallPermissionListener() {
                        @Override
                        public void permissionSuccess() {
                            installApk();
                        }

                        @Override
                        public void permissionFail() {
                            Toast.makeText(UpdateService.this, "授权失败，无法安装应用", Toast.LENGTH_LONG).show();
                        }
                    };
                    Intent intent = new Intent(this, InstallPermissionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    return intent;
                }
            }
        } else {
            installIntent.setDataAndType(Uri.parse("file://" + apkFile.getPath()),
                    "application/vnd.android.package-archive");
        }
        return installIntent;
    }

    /**
     * 8.0 权限获取后的安装
     */
    private void installApk() {
        File apkFile = new File(filePath);
        // 通过Intent安装APK文件
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri apkUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", apkFile);
        installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        startActivity(installIntent);
    }

    /**
     * 删除无用apk文件
     */
    private void deleteApkFile() {
        File apkFile = new File(filePath);
        if (apkFile.exists() && apkFile.isFile()) {
            apkFile.delete();
        }
    }
}
