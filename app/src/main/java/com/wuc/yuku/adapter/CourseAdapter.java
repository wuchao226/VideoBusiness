package com.wuc.yuku.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.wuc.sdk.core.VideoManagerInterface;
import com.wuc.sdk.core.video.VideoManager;
import com.wuc.sdk.utils.ImageLoaderUtil;
import com.wuc.sdk.utils.Utils;
import com.wuc.sdk.widget.CustomVideoView;
import com.wuc.yuku.R;
import com.wuc.yuku.activity.PhotoViewActivity;
import com.wuc.yuku.module.recommend.RecommendBodyValue;
import com.wuc.yuku.util.Util;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: wuchao
 * @date: 2018/12/7 11:32
 * @desciption:
 */
public class CourseAdapter extends BaseAdapter {

    /**
     * ListView 不同类型的item标识
     */
    private static final int CARD_COUNT = 4;
    private static final int VIDOE_TYPE = 0x00;
    private static final int CARD_TYPE_ONE = 0x01;
    private static final int CARD_TYPE_TWO = 0x02;
    private static final int CARD_TYPE_THREE = 0x03;

    private LayoutInflater mInflater;
    private Context mContext;
    private ViewHolder mViewHolder;
    private ArrayList<RecommendBodyValue> mData;
    private VideoManager mVideoManager;
    private ImageLoaderUtil mImageLoaderUtil;

    public CourseAdapter(Context context, ArrayList<RecommendBodyValue> data) {
        mContext = context;
        mData = data;
        mImageLoaderUtil = ImageLoaderUtil.getInstance(context);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        final RecommendBodyValue value = (RecommendBodyValue) getItem(position);
        //无tag
        if (convertView == null) {
            switch (type) {
                case VIDOE_TYPE:
                    mViewHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_video_layout, parent, false);
                    mViewHolder.mVieoContentLayout = (RelativeLayout)
                            convertView.findViewById(R.id.video_ad_layout);
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mShareView = (ImageView) convertView.findViewById(R.id.item_share_view);
                    //为对应布局创建播放器

                    mVideoManager = new VideoManager(mViewHolder.mVieoContentLayout,
                            JSON.toJSONString(value), new CustomVideoView.FrameImageLoadListener() {
                        @Override
                        public void onStartFrameLoad(String url, CustomVideoView.ImageLoaderListener listener) {
                        }
                    });
                    mVideoManager.setListener(new VideoManagerInterface() {
                        @Override
                        public void onVideoSuccess() {

                        }

                        @Override
                        public void onVideoFailed() {

                        }

                        @Override
                        public void onClickVideo(String url) {
//                            Intent intent = new Intent(mContext, AdBrowserActivity.class);
//                            intent.putExtra(AdBrowserActivity.KEY_URL, url);
//                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case CARD_TYPE_ONE:
                    mViewHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_product_card_one_layout, parent, false);
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    mViewHolder.mProductLayout = (LinearLayout) convertView.findViewById(R.id.product_photo_layout);
                    break;
                case CARD_TYPE_TWO:
                    mViewHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_product_card_two_layout, parent, false);
                    mViewHolder.mLogoView = (CircleImageView) convertView.findViewById(R.id.item_logo_view);
                    mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
                    mViewHolder.mInfoView = (TextView) convertView.findViewById(R.id.item_info_view);
                    mViewHolder.mFooterView = (TextView) convertView.findViewById(R.id.item_footer_view);
                    mViewHolder.mProductView = (ImageView) convertView.findViewById(R.id.product_photo_view);
                    mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.item_price_view);
                    mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.item_from_view);
                    mViewHolder.mZanView = (TextView) convertView.findViewById(R.id.item_zan_view);
                    break;
                case CARD_TYPE_THREE:
                    mViewHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_product_card_three_layout, null, false);
                    mViewHolder.mViewPager = (ViewPager) convertView.findViewById(R.id.pager);
                    ArrayList<RecommendBodyValue> recommendList = Util.handleData(value);
                    mViewHolder.mViewPager.setPageMargin(Utils.dip2px(mContext, 12));
                    mViewHolder.mViewPager.setAdapter(new HotSalePagerAdapter(mContext, recommendList));
                    mViewHolder.mViewPager.setCurrentItem(recommendList.size() * 100);
                    break;
                default:
            }
            convertView.setTag(mViewHolder);
        }
        //有tag
        else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        //填充item的数据
        switch (type) {
            case VIDOE_TYPE:
                mImageLoaderUtil.displayImage(mViewHolder.mLogoView, value.logo);
                mViewHolder.mTitleView.setText(value.title);
                mViewHolder.mInfoView.setText(value.info.concat(mContext.getString(R.string.tian_qian)));
                mViewHolder.mFooterView.setText(value.text);
                mViewHolder.mShareView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ShareDialog dialog = new ShareDialog(mContext, false);
//                        dialog.setShareType(Platform.SHARE_VIDEO);
//                        dialog.setShareTitle(value.title);
//                        dialog.setShareTitleUrl(value.site);
//                        dialog.setShareText(value.text);
//                        dialog.setShareSite(value.title);
//                        dialog.setShareTitle(value.site);
//                        dialog.setUrl(value.resource);
//                        dialog.show();
                    }
                });
                break;
            case CARD_TYPE_ONE:
                mImageLoaderUtil.displayImage(mViewHolder.mLogoView, value.logo);
                mViewHolder.mTitleView.setText(value.title);
                mViewHolder.mInfoView.setText(value.info.concat(mContext.getString(R.string.tian_qian)));
                mViewHolder.mFooterView.setText(value.text);
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
                mViewHolder.mZanView.setText(mContext.getString(R.string.dian_zan).concat(value.zan));
                mViewHolder.mProductLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhotoViewActivity.class);
                        intent.putStringArrayListExtra(PhotoViewActivity.PHOTO_LIST, value.url);
                        mContext.startActivity(intent);
                    }
                });
                mViewHolder.mProductLayout.removeAllViews();
                //动态添加多个imageview
                for (String url : value.url) {
                    mViewHolder.mProductLayout.addView(createImageView(url));
                }
                break;
            case CARD_TYPE_TWO:
                mImageLoaderUtil.displayImage(mViewHolder.mLogoView, value.logo);
                mViewHolder.mTitleView.setText(value.title);
                mViewHolder.mInfoView.setText(value.info.concat(mContext.getString(R.string.tian_qian)));
                mViewHolder.mFooterView.setText(value.text);
                mViewHolder.mPriceView.setText(value.price);
                mViewHolder.mFromView.setText(value.from);
                mViewHolder.mZanView.setText(mContext.getString(R.string.dian_zan).concat(value.zan));
                //为单个ImageView加载远程图片
                mImageLoaderUtil.displayImage(mViewHolder.mProductView, value.url.get(0));
                break;
            case CARD_TYPE_THREE:
                break;
            default:
        }
        return convertView;
    }

    /**
     * 通知adapter使用哪种类型来加载数据
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        RecommendBodyValue value = (RecommendBodyValue) getItem(position);
        return value.type;
    }

    /**
     * 动态添加ImageView
     *
     * @param url
     * @return
     */
    private ImageView createImageView(String url) {
        ImageView photoView = new ImageView(mContext);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(Utils.dip2px(mContext, 100),
                        LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin = Utils.dip2px(mContext, 5);
        photoView.setLayoutParams(params);
        mImageLoaderUtil.displayImage(photoView, url);
        return photoView;
    }

    @Override
    public int getViewTypeCount() {
        return CARD_COUNT;
    }

    /**
     * 自动播放方法
     */
    public void updateVideoInScrollView() {
        if (mVideoManager != null) {
            mVideoManager.updateVideoInScrollView();
        }
    }

    private static class ViewHolder {
        /**
         * 所有Card共有属性
         */
        private CircleImageView mLogoView;
        private TextView mTitleView;
        private TextView mInfoView;
        private TextView mFooterView;
        /**
         * Video Card特有属性
         */
        private RelativeLayout mVieoContentLayout;
        private ImageView mShareView;

        /**
         * Video Card外所有Card具有属性
         */
        private TextView mPriceView;
        private TextView mFromView;
        private TextView mZanView;
        /**
         * Card One特有属性
         */
        private LinearLayout mProductLayout;
        /**
         * Card Two特有属性
         */
        private ImageView mProductView;
        /**
         * Card Three特有属性
         */
        private ViewPager mViewPager;
    }
}
