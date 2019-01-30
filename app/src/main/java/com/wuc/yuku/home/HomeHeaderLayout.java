package com.wuc.yuku.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuc.sdk.utils.ImageLoaderUtil;
import com.wuc.yuku.R;
import com.wuc.yuku.module.recommend.RecommendFooterValue;
import com.wuc.yuku.module.recommend.RecommendHeadValue;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

/**
 * @author: wuchao
 * @date: 2018/12/8 23:17
 * @desciption:
 */
public class HomeHeaderLayout extends RelativeLayout {

    private Context mContext;

    /**
     * UI
     */
    private RelativeLayout mRootView;
    private Banner mBanner;
    private TextView mHotView;
    private ImageView[] mImageViews = new ImageView[4];
    private LinearLayout mFootLayout;
    /**
     * Data
     */
    private RecommendHeadValue mHeaderValue;
    private ImageLoaderUtil mImageLoader;

    public HomeHeaderLayout(Context context, RecommendHeadValue headerValue) {
        this(context, null, headerValue);
    }

    public HomeHeaderLayout(Context context, AttributeSet attrs, RecommendHeadValue headerValue) {
        super(context, attrs);
        mContext = context;
        mHeaderValue = headerValue;
        mImageLoader = ImageLoaderUtil.getInstance(mContext);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mRootView = (RelativeLayout) inflater.inflate(R.layout.listview_home_head_layout, this);
        mBanner = mRootView.findViewById(R.id.banner);
        mHotView = (TextView) mRootView.findViewById(R.id.zuixing_view);
        mImageViews[0] = (ImageView) mRootView.findViewById(R.id.head_image_one);
        mImageViews[1] = (ImageView) mRootView.findViewById(R.id.head_image_two);
        mImageViews[2] = (ImageView) mRootView.findViewById(R.id.head_image_three);
        mImageViews[3] = (ImageView) mRootView.findViewById(R.id.head_image_four);
        mFootLayout = (LinearLayout) mRootView.findViewById(R.id.content_layout);

        mBanner.setImageLoader(new BannerImageLoader())
                .setImages(mHeaderValue.ads)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
//                        Intent intent = new Intent(mContext,
//                                CourseDetailActivity.class);
//                        mContext.startActivity(intent);
                    }
                })
                .start();
        for (int i = 0; i < mImageViews.length; i++) {
            mImageLoader.displayImage(mImageViews[i], mHeaderValue.middle.get(i));
        }
        for (RecommendFooterValue value : mHeaderValue.footer) {
            mFootLayout.addView(createBottomItem(value));
        }
        mHotView.setText(mContext.getString(R.string.today_zuixing));
    }

    private HomeBottomItem createBottomItem(RecommendFooterValue value) {
        return new HomeBottomItem(mContext, value);
    }
}

class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        ImageLoaderUtil.getInstance(context).displayImage(imageView, (String) path);
    }
}
