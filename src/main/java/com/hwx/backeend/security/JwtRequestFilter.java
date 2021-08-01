package com.hwx.backeend.security;

import com.hwx.backeend.service.MyUserDetailsService;
import com.hwx.backeend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        if (request.getRequestURI().equals("/api/auth/login")) {
            try {
                chain.doFilter(request, response);
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }
            return;
        }
//        logger.debug(request.requestURI)
        String bearerToken = request.getHeader("Authorization");
        String username = null;
        String token = null;
        // 去除 Bearer 开头
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.warn("找不到 JWT");
            } catch (Exception e) {
                logger.warn("这里有错误 ${e.message}");
                throw e;
            }
//            if (jwtTokenUtil.isTokenExpired(token)) {
//                logger.warn("JWT 已过期");
//                throw TokenExpireException("JWT 已过期");
//            }
        } else {
            logger.warn("你这个请求头格式不对啊,没有 Bearer 开头");
        }

        // 验证一下
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // 拿到用户名开干
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        try {
            chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }

    }
}
