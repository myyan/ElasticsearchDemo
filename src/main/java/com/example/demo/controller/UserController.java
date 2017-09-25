package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public User postUser(@RequestBody User user) {
        log.info("post user:{}", user);
        return userService.save(user);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseBody
    public List<User> deleteUser(@RequestParam(value = "name") String name) {
        log.info("delete by name :{}", name);
        return userService.deleteByName(name);
    }

    @RequestMapping(value = "",method = RequestMethod.PUT)
    @ResponseBody
    public List<User> updateUser(@RequestParam(value = "name") @Valid @NotBlank String name){
        log.info("update by name:{}",name);
        return userService.updateByName(name);
    }
}
