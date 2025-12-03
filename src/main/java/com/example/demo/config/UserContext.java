package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;

/**
 * 用户上下文，用于在整个请求链路中共享用户信息
 */
public class UserContext {

    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public static User getCurrentUser() {
        User user = currentUser.get();
        if (user == null) {
            throw new ServiceException("用户未登录或会话已过期", 1004);
        }
        return user;
    }

    public static Integer getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    public static void clear() {
        currentUser.remove();
    }
}