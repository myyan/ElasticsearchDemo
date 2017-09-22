package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by heiqie on 2017/9/22.
 */
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
