package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by heiqie on 2017/9/22.
 */
@Controller
@Slf4j
@RequestMapping(value = "/user")
public class UserController {


    @Autowired
    private UserService userService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    @ResponseBody
    public User queryByName(@RequestParam(value = "name") String name){
        log.info(" query by name ,name :{}",name);
        return userService.findByName(name);
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    public User postUser(@RequestBody User user){
        log.info("post user:{}",user);
        return userService.save(user);
    }
}
