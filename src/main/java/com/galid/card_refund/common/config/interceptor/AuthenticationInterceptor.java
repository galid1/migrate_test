package com.galid.card_refund.common.config.interceptor;

import com.galid.card_refund.common.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");

        if(authorization == null)
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization Header가 존재하지 않습니다.");

        validateToken(authorization);
        return true;
    }

    private void validateToken(String authorizationHeader) {
        String token = authorizationHeader.split(" ")[1];
        jwtUtil.validateToken(token);
    }
}
