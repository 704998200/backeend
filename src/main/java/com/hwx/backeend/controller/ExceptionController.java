package com.hwx.backeend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
@RestController
public class ExceptionController {
    @RequestMapping("/error/throw")
    void throwException(HttpServletRequest request) {
        try {
            throw new Exception((Exception)request.getAttribute("filter.error") );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
