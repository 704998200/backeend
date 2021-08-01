package com.hwx.backeend;

import com.hwx.backeend.entity.User;
import com.hwx.backeend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BackeendApplicationTests {
    @Autowired
    UserRepository userRepository;

    @Test
    void contextLoads() {
        List<User> users =userRepository.searchUserByUsername("gzzchh2");
        for(User user:users){
            user.getId();
        }
    }

}
