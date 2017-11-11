package com.example.demo.service.core;

import com.example.demo.service.api.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by heiqie on 2017/11/10.
 */
@Slf4j
abstract class Consumer<T> extends Thread {

    /**
     * 批量数据容器
     */
    protected List<T> container;

    /**
     * 队列大小
     */
    protected int queueSize;

    /**
     * 队列
     */
    protected ConcurrentLinkedQueue<T> queue;

    /**
     * 当前容器数据量游标
     */
    protected int index = 0;

    /**
     * 业务服务 保存service
     */
    protected UserService userService;

    /**
     * 当前线程是否执行完成
     */
    protected boolean completed = false;

    /**
     * 当前线程是否空闲
     * 正在从队列捞数据保存  idle = false
     * 队列为空  idle = true
     */
    protected boolean idle = false;

    public Consumer(int queueSize, ConcurrentLinkedQueue<T> queue, UserService userService) {
        this.queueSize = queueSize;
        this.queue = queue;
        this.userService = userService;
    }

    /**
     * 初始化consumer相关参数
     */
    public void init() {
        this.container = new ArrayList<>(queueSize);
        postConstruct();
    }


    /**
     * consumer 处理
     */
    public void process(){
        if (!queue.isEmpty()) {
            idle =false;
            container.add(queue.poll());
            index++;
            checkIfNeedSave();
        } else {
            idle = true;
        }
    }

    /**
     * 初始化参数的时候做一些需要的处理
     */
    public abstract void postConstruct();

    /**
     * 线程退出之前做一些需要的处理
     */
    public abstract void preDestroy();


    /**
     * 主线程结束时调用  子线程释放资源
     */
    public void finish(){
        log.info("job will finished several seconds later ,prepare save remained data ");
        while (!idle) {
            log.info("run run");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (index != 0) {
            log.info("remained index:{}", index);
            batchSave(container);
            container.clear();
            index = 0;
        }
        completed = true;
        preDestroy();
        log.info("remained data saved success");
    }

    /**
     * 调用具体业务批量保存接口
     * @param container
     */
    public abstract void batchSave(List<T> container);


    /**
     * 检测index有没有到最大限制
     */
    private void checkIfNeedSave(){
        if (index == queueSize) {
            log.info("index :{}", index);
            batchSave(container);
            container.clear();
            index = 0;
            log.info("batch save success");
        }
    }


}
