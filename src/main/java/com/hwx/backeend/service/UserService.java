package com.hwx.backeend.service;

import com.hwx.backeend.entity.Role;
import com.hwx.backeend.entity.User;
import com.hwx.backeend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User findUserByUsername(String username) {
        if (checkUserExists(username)) {
            return userRepository.findUserByUsername(username);
        } else {
            try {
                throw new Exception("FUck!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    Boolean checkUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

//    List<Role> findRolesByUser(User user) {
//        //TODO("Not yet implemented")
//    }

    public User findById(Long postedByUserId) {
        return userRepository.findById(postedByUserId).get();
    }

    public Integer deleteById(Long userId) {
        try{
        userRepository.deleteById(userId);}
        catch (Exception e) {
            return 1;
        }
        return 0;

    }

    public Integer save(User user) {
        try{
        userRepository.save(user);
        }catch (Exception e) {
            return 1;
        }
        return 0;
    }

    public List<User> searchUserByUsername(String keyword)  {
        return userRepository.searchUserByUsername(keyword);
    }


}
