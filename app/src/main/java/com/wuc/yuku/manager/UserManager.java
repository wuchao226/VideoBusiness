package com.wuc.yuku.manager;

import com.wuc.yuku.module.user.User;

/**
 * @author: wuchao
 * @date: 2019/1/3 15:48
 * @desciption: 单例来管理登录用户信息
 */
public class UserManager {

    private User mUser = null;

    public static UserManager getInstance() {
        return Holder.INSTANCE;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    /**
     * 用户是否登录
     */
    public boolean hasLogined() {
        return mUser != null;
    }

    private void removeUser() {
        mUser = null;
    }

    private static class Holder {
        private static final UserManager INSTANCE = new UserManager();
    }
}
