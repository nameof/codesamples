package com.nameof.multisecurity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * 基于接口密钥认证
 * @Author: chengpan
 * @Date: 2020/2/23
 */
public class KeyAuthorizationFilter extends BasicAuthenticationFilter {

    public KeyAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
        if (authentication == null) {
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //允许本地
        if (request.getLocalAddr().equals(request.getRemoteAddr()))
            return new UsernamePasswordAuthenticationToken("local", null, Collections.emptyList());

        String key = extractToken(request);
        if (key == null) {
            error(response, 401, "密钥为空");
            return null;
        }

         if (!checkKey(key)) {
             error(response, 403, "密钥不合法");
             return null;
         }

        if (!checkIp()) {
            error(response, 403, "调用IP不合法");
            return null;
        }

        return new UsernamePasswordAuthenticationToken(key, null, Collections.emptyList());
    }

    private boolean checkKey(String key) {
        return true;
    }

    private boolean checkIp() {
        //...
        return true;
    }

    private void error(HttpServletResponse response, int code, String message) throws IOException {
        response.setStatus(code);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(message);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring("Bearer ".length());
    }
}
