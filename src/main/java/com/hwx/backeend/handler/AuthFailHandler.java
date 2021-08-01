package com.hwx.backeend.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AuthFailHandler implements AuthenticationFailureHandler {
   public void onAuthenticationFailure(
           HttpServletRequest request,
           HttpServletResponse response ,
           AuthenticationException exception
    ) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
       Map <String, Object>data = new HashMap();
        data.put("timestamp",Calendar.getInstance().getTime()) ;
        data.put("exception",exception.getMessage()) ;
//        response.outputStream
//            .println(objectMapper.writeValueAsString(data))
    }
}
