package com.vv.bi.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.vv.bi.annotation.AuthCheck;
import com.vv.bi.common.BaseResponse;
import com.vv.bi.common.DeleteRequest;
import com.vv.bi.common.ErrorCode;
import com.vv.bi.common.ResultUtils;
import com.vv.bi.constant.CommonConstant;
import com.vv.bi.constant.UserConstant;
import com.vv.bi.exception.BusinessException;
import com.vv.bi.exception.ThrowUtils;
import com.vv.bi.manager.AiManager;
import com.vv.bi.manager.RedisLimiterManager;
import com.vv.bi.model.dto.chart.*;
import com.vv.bi.model.entity.Chart;
import com.vv.bi.model.entity.User;
import com.vv.bi.model.vo.AiResponse;
import com.vv.bi.service.ChartService;
import com.vv.bi.service.UserService;
import com.vv.bi.utils.ExcelUtils;
import com.vv.bi.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 队列测试
 *
 */
@RestController
@RequestMapping("/queue")
@Slf4j
@Profile({"dev","local"})
public class QueueController {
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

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
}
