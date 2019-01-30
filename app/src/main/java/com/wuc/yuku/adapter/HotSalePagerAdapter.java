package com.wuc.yuku.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuc.sdk.utils.ImageLoaderUtil;
import com.wuc.yuku.R;
import com.wuc.yuku.activity.CourseDetailActivity;
import com.wuc.yuku.module.recommend.RecommendBodyValue;

import java.util.ArrayList;

/**
 * @author: wuchao
 * @date: 2018/12/7 17:22
 * @desciption:
 */
public class HotSalePagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<RecommendBodyValue> mData;
    private LayoutInflater mInflater;
    private ImageLoaderUtil mImageLoaderUtil;

    public HotSalePagerAdapter(Context context, ArrayList<RecommendBodyValue> data) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(context);
        mImageLoaderUtil = ImageLoaderUtil.getInstance(context);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final RecommendBodyValue value = mData.get(position % mData.size());
        View rootView = mInflater.inflate(R.layout.item_hot_product_pager_layout, null);
        TextView titleView = (TextView) rootView.findViewById(R.id.title_view);
        TextView infoView = (TextView) rootView.findViewById(R.id.info_view);
        TextView gonggaoView = (TextView) rootView.findViewById(R.id.gonggao_view);
        TextView saleView = (TextView) rootView.findViewById(R.id.sale_num_view);
        ImageView[] imageViews = new ImageView[3];
        imageViews[0] = rootView.findViewById(R.id.image_one);
        imageViews[1] = rootView.findViewById(R.id.image_two);
        imageViews[2] = rootView.findViewById(R.id.image_three);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CourseDetailActivity.class);
                intent.putExtra(CourseDetailActivity.COURSE_ID, value.adid);
                mContext.startActivity(intent);
            }
        });

        titleView.setText(value.title);
        infoView.setText(value.price);
        gonggaoView.setText(value.info);
        saleView.setText(value.text);
        for (int i = 0; i < imageViews.length; i++) {
            mImageLoaderUtil.displayImage(imageViews[i], value.url.get(i));
        }
        container.addView(rootView, 0);
        return rootView;
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
