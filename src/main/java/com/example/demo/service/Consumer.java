package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by heiqie on 2017/11/10.
 */
abstract class Consumer<T> extends Thread {

    /**
     * 批量数据容器
     */
    protected List<T> container;

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
     * 业务服务 保存
     */
    protected UserService userService;

    /**
     * 当前线程
     */
    protected boolean alive = true;

    /**
     * 当前线程是否空闲
     */
    protected boolean isKongxian = false;

    public Consumer(int queueSize, ConcurrentLinkedQueue<T> queue, UserService userService) {
        this.queueSize = queueSize;
        this.queue = queue;
        this.userService = userService;
    }

    public void init() {
        this.container = new ArrayList<>(queueSize);
    }

    public void process() {
        init();
        doProcess();
        preDestroy();
    }

    public abstract void doProcess();

    public abstract void preDestroy();

    public abstract void finish();


}
