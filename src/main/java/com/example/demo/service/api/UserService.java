package com.example.demo.service.api;

import com.example.demo.model.User;

import java.util.List;

/**
 * Created by heiqie on 2017/9/22.
 */
public interface UserService {

    List<User> findByName(String name);

    User save(User user);

    void save(List<User> users);

    List<User> deleteByName(String name);

    List<User> updateByName(String name);


}
