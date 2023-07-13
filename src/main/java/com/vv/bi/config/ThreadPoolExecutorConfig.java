package com.vv.bi.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author vv
 * @Description 线程池配置类
 * @date 2023/7/12-12:43
 */
@Configuration
public class ThreadPoolExecutorConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        ThreadFactory threadFactory = new ThreadFactory() {
            private int cnt = 1;
            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("线程" + cnt++);
                return thread;
            }
        };
        //差一个参数，拒绝策略。默认拒绝任务
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,
                4,
                100,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4),threadFactory);
        return threadPoolExecutor;
    }
}
