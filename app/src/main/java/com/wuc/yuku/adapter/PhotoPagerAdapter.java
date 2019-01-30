package com.wuc.yuku.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.wuc.sdk.utils.ImageLoaderUtil;

import java.util.ArrayList;

/**
 * @author: wuchao
 * @date: 2019/1/4 15:45
 * @desciption: 大图适配器
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> mData;
    private ImageLoaderUtil mImageLoaderUtil;

    public PhotoPagerAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
        mImageLoaderUtil = ImageLoaderUtil.getInstance(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mContext);
        mImageLoaderUtil.displayImage(photoView, mData.get(position));
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
