package com.wuc.yuku.service.update;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author: wuchao
 * @date: 2018/12/27 13:38
 * @desciption: 下载调度管理器，调用UpdateDownloadRequest
 */
public class UpdateManager {
    /**
     * 线程池
     */
    private ExecutorService mExecutorService;
    private UpdateDownloadRequest mDownloadRequest;

    private UpdateManager() {
        //创建cache线程池
        mExecutorService = Executors.newCachedThreadPool();
    }

    public static UpdateManager getInstance() {
        return Holder.INSTANCE;
    }

    public void startDownload(String downloadUrl, String localFilePath, UpdateDownloadListener listener) {
        if (mDownloadRequest != null && mDownloadRequest.isDownloading()) {
            return;
        }
        checkLocalFilePath(localFilePath);
        mDownloadRequest = new UpdateDownloadRequest(downloadUrl, localFilePath, listener);
        Future<?> future = mExecutorService.submit(mDownloadRequest);
        new WeakReference<Future<?>>(future);
    }

    /**
     * 检查文件路径
     */
    private void checkLocalFilePath(String localFilePath) {
        File path = new File(localFilePath.substring(0,
                localFilePath.lastIndexOf("/") + 1));
        File file = new File(localFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface InstallPermissionListener {
        /**
         * 权限申请成功
         */
        void permissionSuccess();

        /**
         * 权限申请失败
         */
        void permissionFail();
    }

    private static class Holder {
        private static final UpdateManager INSTANCE = new UpdateManager();
    }
}
