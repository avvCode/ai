package com.vv.analyze.config;

import com.vv.analyze.constant.MqConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vv
 * @Description TODO
 * @date 2023/8/6-17:26
 */
@Configuration
public class RabbitMqConfig {
    @Bean(MqConstant.QUEUE_NAME)
    public Queue analyzeQueue(){
        return new Queue(MqConstant.QUEUE_NAME);
    }

    @Bean(MqConstant.EXCHANGE_NAME)
    public Exchange analyzeExchange(){
        return ExchangeBuilder
                .topicExchange(MqConstant.EXCHANGE_NAME)
                .durable(true)
                .build();
    }
    @Bean
    public Binding BindQueueAndExchange (@Qualifier(MqConstant.QUEUE_NAME) Queue queue,@Qualifier(MqConstant.EXCHANGE_NAME) Exchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(MqConstant.ROUTING_KEY)
                .noargs();
    }
}
