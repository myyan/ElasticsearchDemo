package com.example.demo.service.core;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by heiqie on 2017/9/22.
 */
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;


    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void save(List<User> users) {
        userRepository.saveAll(users);
    }

    public List<User> deleteByName(String name){
        return userRepository.deleteByName(name);
    }

    public List<User> updateByName(String name) {
        List<User> users = userRepository.findByName(name);
        for (User user :users){
            user.setPhone("13127700123");
            user.setAge(22);
        }
        userRepository.saveAll(users);
        return users;
    }
}
