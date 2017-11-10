package com.example.demo.service;

import com.example.demo.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
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
    public void doProcess() {
        if (!queue.isEmpty()) {
            isKongxian=false;
            container.add(queue.poll());
            index++;
            checkIfNeedSave();
        } else {
            isKongxian = true;
        }
    }

    private void checkIfNeedSave() {
        if (index == queueSize) {
            log.info("index :{}", index);
            userService.save(container);
            container.clear();
            index = 0;
            log.info("batch save success");
        }
    }

    @Override
    public void preDestroy() {

    }

    @Override
    public void finish() {
        log.info("job will finished several seconds later ,current index:{},prepare save remained data ", index);
        while (!isKongxian) {
            log.info("run run");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (index != 0) {
            log.info("remained index:{}",index);
            userService.save(container);
            container.clear();
            index = 0;
        }
        alive = false;
        log.info("remained data saved success");
    }

    public static Consumer begin(int queueSize, ConcurrentLinkedQueue blockingQueue, UserService userService) {
        UserConsumer consumer = new UserConsumer(queueSize, blockingQueue, userService);
        consumer.start();
        return consumer;
    }

    @Override
    public void run() {
        log.info("start process");
        process();
        while (alive) {
            doProcess();
        }
        log.info("exit");
    }


}
