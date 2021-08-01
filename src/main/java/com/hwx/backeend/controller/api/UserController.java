package com.hwx.backeend.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.hwx.backeend.entity.User;
import com.hwx.backeend.result.JsonResult;
import com.hwx.backeend.service.MyUserDetailsService;
import com.hwx.backeend.service.UserService;
import com.hwx.backeend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserService userService;

    @Autowired
    MyUserDetailsService userDetailsService;

    @PreAuthorize("permitAll()")
    @RequestMapping("/get")
    JsonResult getCurrentUser(HttpServletRequest request) {
        String token = jwtTokenUtil.getToken(request.getHeader("Authorization"));
        String username =  jwtTokenUtil.getUsernameFromToken(token) ;
        User user = userDetailsService.loadOnlyUserByUsername(username);
        // 填充用户信息
        JSONObject userInfo =new JSONObject();
        userInfo.put("username",user.getUsername());
        userInfo.put("email",user.getEmail());
        userInfo.put("roles",user.roles);
        return new JsonResult(0, userInfo);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/{userId}/get")
    JsonResult getSpecUser(@PathVariable String userId) {
        userService.deleteById(Long.valueOf(userId));
        return new JsonResult(0, "");
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/{userId}/update")
    JsonResult updateUser(
            @PathVariable String userId ,
            @RequestBody JSONObject jsonParam
    ) {
        User user = userService.findById(Long.valueOf(userId));
        user.setUsername(jsonParam.getString("username"));
        user.setFirstname(jsonParam.getString("firstname"));
        user.setLastname(jsonParam.getString("lastname"));
        user.setEmail(jsonParam.getString("email"));
        userService.save(user);
        return new JsonResult(0, "");
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/search")
    JsonResult  searchUserByUsername(@RequestBody JSONObject jsonParam ) {
        String keyword = jsonParam.getString("keyword");
        List<User> users = userService.searchUserByUsername(keyword);
        return new JsonResult(0, users);
    }

    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value="/{userId}/delete")
    JsonResult deleteUser(@PathVariable String userId)  {
        int result = userService.deleteById(Long.valueOf(userId));
        return new JsonResult(0, result);
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(value="/register")
    JsonResult register(@RequestBody JSONObject jsonParam )  {
        String username = jsonParam.getString("username");
        String password = jsonParam.getString("password");
        String email = jsonParam.getString("email");
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setEmail(email);
        int result = userService.save(user);
        return new JsonResult(0, result);

    }
}
