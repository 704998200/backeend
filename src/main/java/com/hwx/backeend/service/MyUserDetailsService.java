package com.hwx.backeend.service;

import com.hwx.backeend.entity.Role;
import com.hwx.backeend.entity.User;
import com.hwx.backeend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    UserRepository userRepository;


    public UserDetails loadUserByUsername(String username) {
         User user = this.loadOnlyUserByUsername(username);
        logger.info("user = {}", user);
        return toUserDetails(user);
    }

    public User loadOnlyUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    UserDetails toUserDetails(User user) {
        List<Role> roles = user.roles;
        Set authorities = new LinkedHashSet();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),
                user.getAccountNonLocked(),
                authorities
        );
    }
}
