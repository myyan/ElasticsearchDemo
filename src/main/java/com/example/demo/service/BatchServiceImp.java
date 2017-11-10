package com.example.demo.service;

import com.example.demo.model.User;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by heiqie on 2017/11/10.
 */
@Service
@Slf4j
public class BatchServiceImp implements BatchService {
    @Autowired
    private UserService userService;

    private static long SIZE = 66666;

    private static final int N_THREADS = 10;

    private static final Integer SUCCESS = 1;

    private static final int QUEUE_SIZE = 10000;

    private ConcurrentLinkedQueue<User> blockingQueue = new ConcurrentLinkedQueue<>();


    @Override
    public void batch() {

        final Stopwatch stopwatch = Stopwatch.createStarted();

        ListeningExecutorService pool = MoreExecutors
                .listeningDecorator(Executors.newFixedThreadPool(N_THREADS));

        log.info("batch start");

        List<Long> userIds = prepareUser();

        final int size = userIds.size();

        log.info("prepare user end,size:{},elapsed {} ms", size, stopwatch.elapsed(TimeUnit.MILLISECONDS));

        List<ListenableFuture<Integer>> futures = new ArrayList();

        final AtomicInteger success = new AtomicInteger(0);

        final AtomicInteger error = new AtomicInteger(0);


        for (final Long userId : userIds) {

            final ListenableFuture<Integer> future = pool.submit(() -> synUser(userId));

            Futures.addCallback(future, new FutureCallback<Integer>() {

                @Override
                public void onSuccess(Integer result) {
                    int successIndex = success.incrementAndGet();
                    if (successIndex % 2500 == 0) {
                        log.info("syn user info with {} of {} records, takes {} ms", successIndex, size, stopwatch.elapsed(TimeUnit.MILLISECONDS));
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    int errorIndex = error.incrementAndGet();
                    log.error("syn user error,error records:{}", errorIndex);
                }
            });

            futures.add(future);

        }

        ListenableFuture<List<Integer>> list = Futures.successfulAsList(futures);

        try {
            List<Integer> integers = list.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("batch error {}", e);
        }

        pool.shutdown();

        log.info("batch end,times:{} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Override
    public void batchByQueue() {

        final Stopwatch stopwatch = Stopwatch.createStarted();

        ListeningExecutorService pool = MoreExecutors
                .listeningDecorator(Executors.newFixedThreadPool(N_THREADS));

        log.info("batch by queue start");

        List<Long> userIds = prepareUser();

        final int size = userIds.size();

        log.info("prepare user end,elapsed {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));

        List<ListenableFuture<Integer>> futures = new ArrayList();

        final AtomicInteger success = new AtomicInteger(0);

        final AtomicInteger error = new AtomicInteger(0);

        List<Consumer> consumers = startConsumer(blockingQueue, QUEUE_SIZE, userService);

        for (final Long userId : userIds) {

            final ListenableFuture<Integer> future = pool.submit(() -> synUserNotSave(userId));

            Futures.addCallback(future, new FutureCallback<Integer>() {

                @Override
                public void onSuccess(Integer result) {
                    int successIndex = success.incrementAndGet();
                    if (successIndex % 1000 == 0) {
                        log.info("syn user info with {} of {} records, takes {} ms", successIndex, size, stopwatch.elapsed(TimeUnit.MILLISECONDS));
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    int errorIndex = error.incrementAndGet();
                    log.error("syn user error,error records:{}", errorIndex);
                }
            });

            futures.add(future);

        }


        ListenableFuture<List<Integer>> list = Futures.successfulAsList(futures);

        try {
            List<Integer> integers = list.get();
            log.info("combine data ok,times:{} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException e) {
            log.error("batch by queue error {}", e);
        }

        consumers.forEach(Consumer::finish);

        pool.shutdown();


        log.info("batch by queue end,times:{} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));


    }

    @Override
    public void changeSize(int size) {
        log.info("change size to :{}", size);
        SIZE = Long.valueOf(size);
    }

    private List<Consumer> startConsumer(ConcurrentLinkedQueue<User> blockingQueue, int queueSize, UserService userService) {
        List<Consumer> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Consumer consumer = UserConsumer.begin(queueSize, blockingQueue, userService);;
            list.add(consumer);
        }
        return list;
    }

    private Integer synUserNotSave(Long userId) {
        User user = getUserById(userId);
        sendToQueue(user);
        return SUCCESS;
    }

    private List<Long> prepareUser() {
        List<Long> result = new ArrayList<>();
        for (long i = 0; i < SIZE; i++) {
            result.add(i);
        }
        return result;
    }


    private User getUserById(Long userId) {
        return User.builder()
                .name("heiqie" + userId)
                .age(22)
                .phone("1312222" + userId)
                .build();
    }

    private Integer synUser(Long userId) {
        userService.save(getUserById(userId));
        log.info("syn user success:{}", userId);
        return SUCCESS;
    }

    private void sendToQueue(User user) {
        blockingQueue.add(user);
    }
}
