package com.wuc.yuku.view.fragment;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.wuc.yuku.constant.Constant;

import java.util.Objects;

/**
 * @author: wuchao
 * @date: 2018/12/4 19:16
 * @desciption: 为所有的Fragment提供公共的行为或事件
 */
public class BaseFragment extends Fragment {

    protected Activity mContext;

    /**
     * 申请指定权限
     */
    public void requestPermission(int code, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, code);
        }
    }

    /**
     * 判断是否有指定权限
     */
    public boolean hasPermission(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.HARDWEAR_CAMERA_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doOpenCamera();
                }
                break;
            case Constant.WRITE_READ_EXTERNAL_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doWriteSDCard();
                }
                break;
            default:
        }
    }

    public void doOpenCamera() {
    }

    public void doWriteSDCard() {

    }
}
