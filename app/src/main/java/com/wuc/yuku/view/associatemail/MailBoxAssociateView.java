package com.wuc.yuku.view.associatemail;


import android.content.Context;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.util.AttributeSet;

/**
 * @author: wuchao
 * @date: 2019/1/3 17:51
 * @desciption: 邮箱联想控件，输入 @ 符后开始联想
 */
public class MailBoxAssociateView extends AppCompatMultiAutoCompleteTextView {

    public MailBoxAssociateView(Context context) {
        super(context);
    }

    public MailBoxAssociateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MailBoxAssociateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 当输入@符号时，就会去调用Tokenizer.findTokenStart()方法一次
     * 当点击下拉提示框中的某个信息时，会再次调用Tokenizer.findTokenStart()方法一次，然后再调用terminateToken()方法一次
     */
    @Override
    public boolean enoughToFilter() {
        // 若用户输入的文本字符串中包含'@'字符且不在第一位，则满足条件返回true，否则返回false
        return getText().toString().contains("@") && getText().toString().indexOf("@") > 0;
    }
}
