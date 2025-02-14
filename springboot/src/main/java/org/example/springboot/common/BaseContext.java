package org.example.springboot.common;

import org.example.springboot.domain.entity.User;

public class BaseContext {
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    /**
     * 获取用户
     *
     * @return 当前用户
     */
    public static User getUser() {
        return threadLocal.get();
    }

    /**
     * 设置用户
     *
     * @param user 当前用户
     */
    public static void setUser(User user) {
        threadLocal.set(user);
    }
}
