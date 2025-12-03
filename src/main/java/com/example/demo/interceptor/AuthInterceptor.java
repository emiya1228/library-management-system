package com.example.demo.interceptor;

import com.example.demo.config.UserContext;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 从Header获取Token
        String token = request.getHeader("authToken");
        if (StringUtils.isEmpty(token)) {
            throw new ServiceException("未提供认证Token", 1002);
        }

        // 2. 从Redis获取Session
        String sessionKey = "session:" + token;
        Map<Object, Object> sessionData = redisTemplate.opsForHash().entries(sessionKey);

        if (sessionData == null || sessionData.isEmpty()) {
            throw new ServiceException("Token已过期或无效", 1003);
        }

        // 3. 将用户信息存入请求上下文
        UserContext.setCurrentUser(convertToUser(sessionData));

        // 4. 刷新Session过期时间（滑动过期）
        redisTemplate.expire(sessionKey, 30, TimeUnit.MINUTES);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // 请求结束后清理线程变量，防止内存泄漏
        UserContext.clear();
    }

    private User convertToUser(Map<Object, Object> sessionData) {
        User user = new User();
        user.setId((Integer) sessionData.get("userId"));
        user.setUsername((String) sessionData.get("username"));
        user.setRole((String) sessionData.get("role"));
        return user;
    }
}
