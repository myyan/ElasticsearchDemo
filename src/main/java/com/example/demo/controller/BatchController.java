package com.example.demo.controller;

import com.example.demo.service.api.BatchService;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

/**
 * Created by heiqie on 2017/11/10.
 */
@Controller
@Slf4j
@RequestMapping(value = "/batch")
@Validated
public class BatchController {


    @Autowired
    private BatchService batchService;


    @RequestMapping(value = "/common")
    @ResponseBody
    public String batch() {
        batchService.batch();
        return "OK";
    }

    @RequestMapping(value = "/queue")
    @ResponseBody
    public String batchByQueue() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        batchService.batchByQueue();
        return "OK" + stopwatch.elapsed(TimeUnit.MILLISECONDS);
    }

    @RequestMapping(value = "/size")
    @ResponseBody
    public String changeSize(@RequestParam(name = "size") int size) {
        batchService.changeSize(size);
        return "OK";
    }
}
