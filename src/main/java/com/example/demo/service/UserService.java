package com.example.demo.service;

import com.example.demo.model.User;

/**
 * Created by heiqie on 2017/9/22.
 */
public interface UserService {
    User findByName(String name);

    User save(User user);
}
