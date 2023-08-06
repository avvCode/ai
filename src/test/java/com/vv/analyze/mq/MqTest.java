package com.vv.analyze.mq;

import com.rabbitmq.client.Channel;
import com.vv.analyze.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.handler.annotation.Header;

import javax.annotation.Resource;

/**
 * @author vv
 * @Description TODO
 * @date 2023/8/6-17:44
 */

@SpringBootTest
@Slf4j
public class MqTest {
    @Resource
    SendMessage sendMessage;

    @Test
    public void test(){
        sendMessage.sendMessage("111");
    }

    @RabbitListener(queues = {MqConstant.QUEUE_NAME},ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        log.info("receive：{}", message);
    }
}
