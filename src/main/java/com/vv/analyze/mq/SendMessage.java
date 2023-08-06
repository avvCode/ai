package com.vv.analyze.mq;

import com.vv.analyze.constant.MqConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author vv
 * @Description 发送消息
 * @date 2023/8/6-16:18
 */

@Component
public class SendMessage {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(MqConstant.EXCHANGE_NAME,MqConstant.ROUTING_KEY,message);

    }

}
