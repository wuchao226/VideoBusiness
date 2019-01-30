package com.wuc.yuku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wuc.sdk.utils.ImageLoaderUtil;
import com.wuc.yuku.R;
import com.wuc.yuku.module.course.CourseCommentValue;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: wuchao
 * @date: 2019/1/8 16:28
 * @desciption:
 */
public class CourseCommentAdapter extends BaseAdapter {

    private LayoutInflater mInflate;
    private Context mContext;

    private ArrayList<CourseCommentValue> mData;
    private ViewHolder mViewHolder;
    private ImageLoaderUtil mImageLoaderUtil;

    public CourseCommentAdapter(Context context, ArrayList<CourseCommentValue> data) {
        mContext = context;
        mData = data;
        mInflate = LayoutInflater.from(mContext);
        mImageLoaderUtil = ImageLoaderUtil.getInstance(mContext);
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
        CourseCommentValue value = (CourseCommentValue) getItem(position);
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflate.inflate(R.layout.item_comment_layout, null);
            mViewHolder.mImageView = (CircleImageView) convertView.findViewById(R.id.photo_view);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.name_view);
            mViewHolder.mCommentView = (TextView) convertView.findViewById(R.id.text_view);
            mViewHolder.mOwnerView = (TextView) convertView.findViewById(R.id.owner_view);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        //填充数据
        if (value.type == 0) {
            mViewHolder.mOwnerView.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.mOwnerView.setVisibility(View.GONE);
        }
        mImageLoaderUtil.displayImage(mViewHolder.mImageView, value.logo);
        mViewHolder.mNameView.setText(value.name);
        mViewHolder.mCommentView.setText(value.text);
        return convertView;
    }
    public void addComment(CourseCommentValue value) {
        mData.add(0, value);
        notifyDataSetChanged();
    }

    public int getCommentCount() {
        return mData.size();
    }

    private static class ViewHolder {

        CircleImageView mImageView;
        TextView mNameView;
        TextView mCommentView;
        TextView mOwnerView;
    }
}
