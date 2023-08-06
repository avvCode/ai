package com.vv.analyze.controller;

import cn.hutool.json.JSONUtil;
import com.vv.analyze.mq.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 队列测试
 *
 */
//@RestController
//@RequestMapping("/queue")
@Slf4j
//@Profile({"dev","local"})
public class QueueController {
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private SendMessage sendMessage;
    @GetMapping("/add")
    public void add(String name){
        CompletableFuture.runAsync(()->{
            log.info("任务执行中：" + name+ "执行人：" + Thread.currentThread().getName());
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },threadPoolExecutor);
    }
    @GetMapping("/get")
    public String get(){
        HashMap<String, Object> hashMap = new HashMap<>();
        int size = threadPoolExecutor.getQueue().size();
        hashMap.put("队列长度",size);
        long taskCount = threadPoolExecutor.getTaskCount();
        hashMap.put("任务总数",taskCount);
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        hashMap.put("已完成任务数",completedTaskCount);
        int activeCount = threadPoolExecutor.getActiveCount();
        hashMap.put("正在工作的线程数",activeCount);
        return JSONUtil.toJsonStr(hashMap);
    }
    @GetMapping("/send")
    public void sendMessage(){
        sendMessage.sendMessage("111");
    }
}
