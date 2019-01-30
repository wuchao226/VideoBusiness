package com.wuc.yuku.jpush;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wuc.yuku.R;

/**
 * @author:     wuchao
 * @date:       2019/1/10 13:53
 * @desciption: 显示推送的消息界面
 */
public class PushMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush_layout);
    }
}
