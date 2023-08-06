package com.vv.analyze.mq;

import com.rabbitmq.client.Channel;
import com.vv.analyze.common.ErrorCode;
import com.vv.analyze.constant.MqConstant;
import com.vv.analyze.exception.BusinessException;
import com.vv.analyze.manager.AiManager;
import com.vv.analyze.model.entity.Chart;
import com.vv.analyze.service.ChartService;
import com.vv.analyze.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

import static com.vv.analyze.constant.CommonConstant.BiModelId;

/**
 * @author vv
 * @Description 消费端
 * @date 2023/8/6-16:21
 */

@Component
@Slf4j
public class DataAnalyzeHandler {
    @Resource
    private ChartService chartService;
    @Resource
    private AiManager aiManager;
    @RabbitListener(queues = {MqConstant.QUEUE_NAME},ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        log.info("receive：{}", message);
        if(message == null){
            try {
                channel.basicNack(deliveryTag,false,false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"消息为空");
        }
        long id = Long.parseLong(message);
        Chart updateChart = new Chart();
        updateChart.setId(id);
        updateChart.setStatus("running");
        boolean b = chartService.updateById(updateChart);
        if(!b){
            try {
                channel.basicNack(deliveryTag,false,false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            handleChartUpdateError(id ,"图表状态执行中更改失败");
        }
        StringBuilder userInput = new StringBuilder();
        Chart chart = chartService.getById(id);
        userInput.append("分析需求：").append("\n");
        String userGoal = chart.getGoal();
        if(StringUtils.isNotBlank(chart.getChartType())){
            userGoal += ",请使用" + chart.getChartType();
        }
        userInput.append(userGoal).append("\n");
        userInput.append(chart.getChartData()).append("\n");

        String result = aiManager.doChat(BiModelId, userInput.toString());
        //对返回结果做拆分
        String[] splits = result.split("【【【【【");
        if(splits.length < 3){
            handleChartUpdateError(id,"AI 生成错误");
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();
        Chart updateChartResult = new Chart();
        updateChartResult.setId(id);
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        updateChartResult.setStatus("succeed");
        boolean b1 = chartService.updateById(updateChartResult);
        if(!b1){
            handleChartUpdateError(id,"图表状态生成成功更改失败");
        }
    }
    private void handleChartUpdateError(long chartId,String execMessage){
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = chartService.updateById(updateChartResult);

    }
}
