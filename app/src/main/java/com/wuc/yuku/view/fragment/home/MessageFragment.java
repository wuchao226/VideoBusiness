package com.wuc.yuku.view.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wuc.sdk.widget.CustomVideoView;
import com.wuc.yuku.R;
import com.wuc.yuku.view.fragment.BaseFragment;

/**
 * @author: wuchao
 * @date: 2018/12/4 19:33
 * @desciption:
 */
public class MessageFragment extends BaseFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_message_layout, container, false);
        RelativeLayout relativeLayout = rootView.findViewById(R.id.content_test);
        CustomVideoView customVideoView2 = new CustomVideoView(getActivity(), relativeLayout);
        customVideoView2.setDataSource("http://fairee.vicp.net:83/2016rm/0116/baishi160116.mp4");
        relativeLayout.addView(customVideoView2);
        return rootView;
    }

}
