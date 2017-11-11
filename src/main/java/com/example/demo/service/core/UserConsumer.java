package com.example.demo.service.core;

import com.example.demo.model.User;
import com.example.demo.service.api.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by heiqie on 2017/11/10.
 */
@Slf4j
public class UserConsumer extends Consumer<User> {


    public UserConsumer(int queueSize, ConcurrentLinkedQueue<User> queue, UserService userService) {
        super(queueSize, queue, userService);
    }

    @Override
    public void postConstruct() {
        log.info("post construct ");
    }


    public static Consumer begin(int queueSize, ConcurrentLinkedQueue blockingQueue, UserService userService) {
        UserConsumer consumer = new UserConsumer(queueSize, blockingQueue, userService);
        consumer.start();
        return consumer;
    }


    @Override
    public void preDestroy() {
        log.info("pre destroy");
    }

    @Override
    public void batchSave(List<User> container) {
        userService.save(container);
    }


    @Override
    public void run() {
        log.info("consumer process start");
        init();
        while (!completed) {
            process();
        }
        log.info("consumer process exit");
    }


}
