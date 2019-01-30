package com.wuc.yuku.view.associatemail;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView;

/**
 * @author: wuchao
 * @date: 2019/1/3 17:53
 * @desciption: 指定从哪个地方开始联想字符
 */
public class MailBoxAssociateTokenizer implements MultiAutoCompleteTextView.Tokenizer {
    /**
     * 用于查找当前光标位置之前的分隔符的位置并返回
     *
     * @param text   用户已经输入的文本内容
     * @param cursor 当前光标的位置，在文本内容后面
     * @return
     */
    @Override
    public int findTokenStart(CharSequence text, int cursor) {
        int index = text.toString().indexOf("@");
        if (index < 0) {
            index = text.length();
        }
        if (index > findTokenEnd(text, cursor)) {
            index = 0;
        }
        return index;
    }

    /**
     * 用于查找当前光标位置之后的分隔符的位置并返回，向后查询
     *
     * @param text   用户已经输入的文本内容
     * @param cursor 当前光标的位置，在文本内容之间
     * @return
     */
    @Override
    public int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();
        // 向后查找'@'字符，若找到则直接返回其所在位置
        while (i < len) {
            if (text.charAt(i) == '@') {
                return i;
            } else {
                i++;
            }
        }
        return len;
    }

    /**
     * 用于返回提示信息加上分隔符后的文本内容
     *
     * @param text
     * @return
     */
    @Override
    public CharSequence terminateToken(CharSequence text) {
        int i = text.length();
        //去掉原始匹配的数据的末尾空格
        while (i > 0 && text.charAt(i - 1) == ' ') {
            i--;
        }
        //判断原始匹配的数据去掉末尾空格后是否含有'@'，有则立即返回
        if (i > 0 && text.charAt(i - 1) == '@') {
            return text;
        } else {
            // CharSequence类型的数据有可能是富文本SpannableString类型
            // 故需要进行判断
            if (text instanceof Spanned) {
                SpannableString sp = new SpannableString(text);
                // 故需要借助TextUtils.copySpansFrom从text中复制原来的样式到新的sp中，
                // 以保持原先样式不变情况下添加一个逗号和空格
                TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                return sp;
            } else {
                return text;
            }
        }
    }
}
