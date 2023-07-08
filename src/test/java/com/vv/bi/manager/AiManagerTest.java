package com.vv.bi.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/8-21:57
 */
@SpringBootTest
class AiManagerTest {
    @Resource
    private AiManager aiManager;
    @Test
    void doChat() {
        String message = "分析需求：\n" +
                "分析网站用户的增长情况\n" +
                "原始数据:\n" +
                "日期,用户数\n" +
                "1号,10\n" +
                "2号,20\n" +
                "3号,30";
        System.out.println(aiManager.doChat(1677647572774281218L,message));
    }
}