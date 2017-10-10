package com.wxsk.vr.mine.common.util;

import com.wxsk.passport.model.User;

import java.util.Date;

public class AppContext {

    private static final ThreadLocal<User> requestThreadUser = new ThreadLocal<>();
    private static final ThreadLocal<Date> requestTimePoint = new ThreadLocal<>();

    /**
     * 设置当前线程User
     */
    public static void setCurrentUser(User user) {
        if (user == null) {
            return;
        }
        requestThreadUser.set(user);
    }

    /**
     * 获取当前线程User
     */
    public static User getCurrentUser() {
        return requestThreadUser.get();
    }

    /**
     * 去除当前线程User
     */
    public static void removeCurrentUser() {
        requestThreadUser.remove();
    }

    /**
     * 设置当前线程系统时间
     */
    public static void setCurrentRequestTimePoint(Date now) {
        requestTimePoint.set(now);
    }

    /**
     * 获取当前线程系统时间
     */
    public static Date getCurrentRequestTimePoint() {
        Date now = requestTimePoint.get();
        return now == null ? new Date() : now;
    }

    /**
     * 去除当前线程系统时间
     */
    public static void removeCurrentRequestTimePoint() {
        requestTimePoint.remove();
    }

    public static void initRequestContext(User user, Date now) {
        setCurrentUser(user);
        setCurrentRequestTimePoint(now);
    }

    public static void clearRequestContext() {
        removeCurrentUser();
        removeCurrentRequestTimePoint();
    }
}
