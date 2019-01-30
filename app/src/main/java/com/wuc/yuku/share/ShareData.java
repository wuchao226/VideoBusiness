package com.wuc.yuku.share;

import cn.sharesdk.framework.Platform;

/**
 * @author: wuchao
 * @date: 2019/1/4 17:59
 * @desciption: 要分享的数据实体
 */
public class ShareData {

    /**
     * 要分享到的平台的参数
     */
    public Platform.ShareParams mShareParams;
    /**
     * 要分享到的平台
     */
    public ShareManager.PlatformType mPlatformType;
}
