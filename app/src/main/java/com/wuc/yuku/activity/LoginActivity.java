package com.wuc.yuku.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuc.sdk.okhttp.listener.DisposeDataListener;
import com.wuc.yuku.R;
import com.wuc.yuku.activity.base.BaseActivity;
import com.wuc.yuku.jpush.PushMessageActivity;
import com.wuc.yuku.manager.UserManager;
import com.wuc.yuku.module.PushMessage;
import com.wuc.yuku.module.user.User;
import com.wuc.yuku.network.RequestCenter;
import com.wuc.yuku.view.associatemail.MailBoxAssociateTokenizer;
import com.wuc.yuku.view.associatemail.MailBoxAssociateView;

/**
 * @author: wuchao
 * @date: 2019/1/3 15:55
 * @desciption: 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 自定义登陆广播Action
     */
    public static final String LOGIN_ACTION = "com.imooc.action.LOGIN_ACTION";
    /**
     * UI
     */
    private MailBoxAssociateView mUserNameAssociateView;
    private EditText mPasswordView;
    private TextView mLoginView;
    /**
     * 用来实现QQ登陆
     */
    private ImageView mQQLoginView;
    /**
     * 推送过来的消息
     */
    private PushMessage mPushMessage;
    /**
     * 是否从推送到此页面
     */
    private boolean fromPush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        initView();
        initData();
    }
    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra("pushMessage")) {
            mPushMessage = (PushMessage) intent.getSerializableExtra("pushMessage");
        }
        fromPush = intent.getBooleanExtra("fromPush", false);
    }

    private void initView() {
        mUserNameAssociateView = findViewById(R.id.associate_email_input);
        mPasswordView = findViewById(R.id.login_input_password);
        mLoginView = findViewById(R.id.login_button);
        mLoginView.setOnClickListener(this);
        mQQLoginView = findViewById(R.id.iv_login_logo);
        mQQLoginView.setOnClickListener(this);

        mUserNameAssociateView = findViewById(R.id.associate_email_input);
        String[] recommendMailBox = getResources().getStringArray(R.array.recommend_mail_box);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_associate_mail_list,
                R.id.tv_recommend_mail, recommendMailBox);
        mUserNameAssociateView.setAdapter(adapter);
        mUserNameAssociateView.setTokenizer(new MailBoxAssociateTokenizer());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                login();
                break;
            //三方登录
            case R.id.iv_login_logo:
                break;
            default:
        }
    }

    /**
     * 发送登录请求
     */
    private void login() {
        String userName = mUserNameAssociateView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            return;
        }

        if (TextUtils.isEmpty(password)) {
            return;
        }

        RequestCenter.login(userName, password, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                User user = (User) responseObj;
                //保存当前用户单例对象
                UserManager.getInstance().setUser(user);
                //发送登录广播
                sendLoginBroadcast();
                if (fromPush) {
                    Intent intent = new Intent(LoginActivity.this, PushMessageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("pushMessage", mPushMessage);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    /**
     * 向整个应用发送登陆广播事件
     */
    private void sendLoginBroadcast() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LOGIN_ACTION));
    }
}
