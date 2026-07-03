package com.tokenshop.interceptor;

import com.tokenshop.common.BusinessException;
import com.tokenshop.common.ResultCode;
import com.tokenshop.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 认证拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final String[] WHITE_LIST = {
            "/api/v1/users",
            "/api/v1/users/login",
            "/doc.html",
            "/swagger-resources",
            "/v2/api-docs",
            "/webjars"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();
        for (String whiteUri : WHITE_LIST) {
            if (uri.startsWith(whiteUri)) {
                return true;
            }
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        String token = authorization.substring(7);
        try {
            Claims claims = JwtUtil.parseToken(token);
            request.setAttribute("userId", claims.get("userId", Long.class));
            request.setAttribute("username", claims.get("username", String.class));
            return true;
        } catch (Exception e) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }
    }
}
