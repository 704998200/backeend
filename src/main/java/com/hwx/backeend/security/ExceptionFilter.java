package com.hwx.backeend.security;

import com.alibaba.fastjson.JSON;
import com.hwx.backeend.result.ErrorMessage;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(ExceptionFilter.class);

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        try {
            try {
                chain.doFilter(request, response);
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }
            logger.debug("进入检查过滤器");
        } catch (ExpiredJwtException e) {
            response.setStatus(403);
            String cause = e.getClass().getName();
            cause = cause.substring(cause.lastIndexOf(".") + 1);
            String result = JSON.toJSONString(new ErrorMessage("1919810", e.getMessage(), cause));
            try {
                response.getWriter().write(result);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            // 异常捕获，发送到error controller
//            request.setAttribute("filter.error", e)
            //将异常分发到/error/throw控制器
//            request.getRequestDispatcher("/error/throw").forward(request, response)
        }
    }
}
