package com.hwx.backeend.handler;

import com.hwx.backeend.result.ErrorMessage;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ControllerExceptionHandler {
    Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    ErrorMessage tokenExpireHandler(
            HttpServletResponse response,
            Exception  e
    ) {
        response.setStatus(403);
        String cause = e.getClass().getName();
        cause = cause.substring(cause.lastIndexOf(".") + 1);
        return new ErrorMessage("1919810", e.getMessage(), cause);
    }

    /**
     * 通用异常处理
     *
     * @param response 结果对象
     * @param e        异常
     * @return 异常信息
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ErrorMessage commonExceptionHandler(HttpServletResponse response , Exception e)  {
        if (logger.isDebugEnabled()) {
            e.printStackTrace();
        }
        String cause = e.getClass().getName();
        cause = cause.substring(cause.lastIndexOf(".") + 1);
        response.setStatus(500);
        return new ErrorMessage("114514", e.getMessage(), cause);
    }

    /**
     * 参数错误异常处理
     *
     * @param response 结果对象
     * @param e        参数错误异常
     * @return 异常信息
     */
    @ExceptionHandler
    @ResponseBody
    ErrorMessage missingServletRequestParameterExceptionHandler(
            HttpServletResponse response ,
            MissingServletRequestParameterException e
    ) {
        response.setStatus(400);
        String cause = e.getClass().getName();
        cause = cause.substring(cause.lastIndexOf(".") + 1);
        return new ErrorMessage("E400", e.getMessage(), cause);
    }
}
