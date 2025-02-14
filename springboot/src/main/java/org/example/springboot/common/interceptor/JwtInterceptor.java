package org.example.springboot.common.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.example.springboot.common.BaseContext;
import org.example.springboot.common.Constants;
import org.example.springboot.common.annotation.Anonymous;
import org.example.springboot.common.enums.ResultCode;
import org.example.springboot.common.exception.CustomException;
import org.example.springboot.domain.entity.User;
import org.example.springboot.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * jwt拦截器
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    String[] whiteList = {"/doc.html", "/webjars/**", "/swagger-resources", "/v2/api-docs"};
    @Resource
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 白名单放行
        String uri = request.getRequestURI();
        if (Arrays.stream(whiteList).anyMatch(url -> PATH_MATCHER.match(url, uri))) {
            return true;
        }
        // 允许匿名访问
        if (handler instanceof HandlerMethod handlerMethod) {
            Anonymous anonymous = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Anonymous.class);
            if (anonymous != null) {
                return true;
            }
        }
        // 获取令牌
        String token = request.getHeader(Constants.TOKEN);
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(Constants.TOKEN);
        }
        // 开始执行认证
        if (StrUtil.isBlank(token)) {
            throw new CustomException(ResultCode.TOKEN_INVALID_ERROR);
        }
        User user;
        try {
            String userId = JWT.decode(token).getAudience().get(0);
            user = userService.getById(Long.valueOf(userId));
            user.setToken(token);
        } catch (Exception e) {
            throw new CustomException(ResultCode.TOKEN_CHECK_ERROR);
        }
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCode.USER_NOT_EXIST_ERROR);
        }
        try {
            // 用户密码加签验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new CustomException(ResultCode.TOKEN_CHECK_ERROR);
        }
        BaseContext.setUser(user);
        return true;
    }
}
