package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heiqie on 2017/9/22.
 */
@Controller
@Slf4j
@RequestMapping(value = "/user")
@Validated
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<User> queryByName(@RequestParam(value = "name") String name) {
        log.info(" query by name ,name :{}", name);
        return userService.findByName(name);
    }

    @RequestMapping(value = "/bulk", method = RequestMethod.POST)
    @ResponseBody
    public String postBulkUser(@RequestBody User user) {
        Long startTime = System.currentTimeMillis();
        List<User> users = new ArrayList<User>();
        log.info("post user:{}", user);
        for (int i = 0; i < 50000; i++) {
            users.add(user);
        }
        userService.save(users);
        Long interval = System.currentTimeMillis() - startTime;
        log.info("timer :{}", interval);
        return "Success time," + interval;
    }


    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public User postUser(@RequestBody User user) {
        Long startTime = System.currentTimeMillis();
        log.info("post user:{}", user);
        User result = userService.save(user);
        Long interval = System.currentTimeMillis() - startTime;
        log.info("timer :{}", interval);
        return result;
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseBody
    public List<User> deleteUser(@RequestParam(value = "name") String name) {
        log.info("delete by name :{}", name);
        return userService.deleteByName(name);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public List<User> updateUser(@RequestParam(value = "name") @Valid @NotBlank String name) {
        log.info("update by name:{}", name);
        return userService.updateByName(name);
    }
}
