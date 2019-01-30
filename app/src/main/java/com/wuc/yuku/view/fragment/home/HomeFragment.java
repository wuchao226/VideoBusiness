package com.wuc.yuku.view.fragment.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.wuc.sdk.okhttp.listener.DisposeDataListener;
import com.wuc.yuku.R;
import com.wuc.yuku.activity.PhotoViewActivity;
import com.wuc.yuku.adapter.CourseAdapter;
import com.wuc.yuku.application.MyApplication;
import com.wuc.yuku.constant.Constant;
import com.wuc.yuku.home.HomeHeaderLayout;
import com.wuc.yuku.module.recommend.BaseRecommendModel;
import com.wuc.yuku.module.recommend.RecommendBodyValue;
import com.wuc.yuku.network.RequestCenter;
import com.wuc.yuku.util.DeviceInfoUtils;
import com.wuc.yuku.view.fragment.BaseFragment;
import com.wuc.yuku.zxing.app.CaptureActivity;

import static android.content.ContentValues.TAG;

/**
 * @author: wuchao
 * @date: 2018/12/4 19:16
 * @desciption:
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final int REQUEST_QRCODE = 0x01;
    /**
     * UI
     */
    private View mContentView;
    private TextView mQrcodeView;
    private TextView mCategoryView;
    private TextView mSearchView;
    private ImageView mLoadingView;
    private ListView mListView;

    /**
     * data
     */
    private CourseAdapter mAdapter;
    private BaseRecommendModel mRecommendModel;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qrcode_view:
                if (hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
                    doOpenCamera();
                } else {
                    requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
                }
                break;
            case R.id.category_view:
                String deviceInfo = DeviceInfoUtils.getDeviceInfo(MyApplication.getInstance());
                Logger.i(TAG, "deviceInfo--->" + deviceInfo);
                break;
            default:
                break;
        }
    }

    @Override
    public void doOpenCamera() {
        Intent intent = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_QRCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_QRCODE:
                if (resultCode == Activity.RESULT_OK) {
                    String code = data.getStringExtra(CaptureActivity.SCAN_RESULT);
                    if (code.contains("http") || code.contains("https")) {
                        /*Intent intent = new Intent(mContext, AdBrowserActivity.class);
                        intent.putExtra(AdBrowserActivity.KEY_URL, code);
                        startActivity(intent);*/
                    } else {
                        Toast.makeText(mContext, code, Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(mContext, code, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_home_layout, container, false);
        initView(mContentView);
        return mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestRecommendData();
    }

    private void requestRecommendData() {
        RequestCenter.requestRecommendData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.e(TAG, "onSuccess:" + responseObj.toString());
                mRecommendModel = (BaseRecommendModel) responseObj;
                //更新UI
                showSuccessView();
            }

            @Override
            public void onFailure(Object reasonObj) {
                Log.e(TAG, "onFailure:" + reasonObj.toString());
                //显示请求失败View
                showErrorView();
            }
        });
    }

    /**
     * 显示请求成功ui
     */
    private void showSuccessView() {
        if (mRecommendModel != null && mRecommendModel.data.list.size() > 0) {
            mLoadingView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mListView.addHeaderView(new HomeHeaderLayout(mContext, mRecommendModel.data.head));
            mAdapter = new CourseAdapter(mContext, mRecommendModel.data.list);
            mListView.setAdapter(mAdapter);
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    mAdapter.updateVideoInScrollView();
                }
            });
        } else {
            showErrorView();
        }
    }

    /**
     * 显示请求失败UI
     */
    private void showErrorView() {
    }

    private void initView(View mContentView) {
        mQrcodeView = (TextView) mContentView.findViewById(R.id.qrcode_view);
        mQrcodeView.setOnClickListener(this);
        mCategoryView = (TextView) mContentView.findViewById(R.id.category_view);
        mCategoryView.setOnClickListener(this);
        mSearchView = (TextView) mContentView.findViewById(R.id.search_view);
        mLoadingView = (ImageView) mContentView.findViewById(R.id.loading_view);
        mListView = (ListView) mContentView.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);

        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //减去列表的头部
        RecommendBodyValue value = (RecommendBodyValue) mAdapter.getItem(
                position - mListView.getHeaderViewsCount());
        if (value.type != 0) {
            Intent intent = new Intent(mContext, PhotoViewActivity.class);
            intent.putStringArrayListExtra(PhotoViewActivity.PHOTO_LIST, value.url);
            startActivity(intent);
        }
    }
}
