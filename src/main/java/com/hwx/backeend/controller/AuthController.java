package com.hwx.backeend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hwx.backeend.entity.User;
import com.hwx.backeend.result.JsonResult;
import com.hwx.backeend.service.MyUserDetailsService;
import com.hwx.backeend.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    MyUserDetailsService userDetailsService;


    Logger logger= LoggerFactory.getLogger(AuthController.class);

    @PreAuthorize("permitAll()")
    @RequestMapping("/login")
    JsonResult login(@RequestBody JSONObject jsonParam ) {
        String username = jsonParam.getString("username");
        String password = jsonParam.getString("password");
        // LY517lX8SfNXFnlx

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            throw e;
        }

        logger.debug("请求了login处理器");
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateTokenFromUserDetail(userDetails);
        Map<String, String> map = new HashMap<String, String>();
        map.put("token",token);
        JsonResult   js=   new JsonResult(0, map);
        String jsonString = JSON.toJSONString(js);
        return js;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/token")
    JsonResult getAuthenticatedUser(HttpServletRequest request, HttpServletResponse resp) {
        String token = jwtTokenUtil.getToken(request.getHeader("Authorization"));
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userDetailsService.loadOnlyUserByUsername(username);
        // 填充用户信息
        JSONObject userInfo = new JSONObject();
        userInfo.put("username",user.getUsername());
        userInfo.put("email",user.getEmail());
        userInfo.put("roles",user.roles);
        return new JsonResult(0, userInfo);
    }

}

