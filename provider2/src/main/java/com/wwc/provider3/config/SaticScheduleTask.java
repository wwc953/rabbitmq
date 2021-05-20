package com.wwc.provider3.config;

import com.alibaba.fastjson.JSONObject;
import com.wwc.provider3.bean.MsgInfo;
import com.wwc.provider3.dao.MsgInfoMapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    @Autowired
    MsgInfoMapper msgInfoMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    //3.添加定时任务
//    @Scheduled(cron = "0/3 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
//        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());

        //查询待发送
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setMsgDesc("01");
        msgInfo.setBizType("order");
        List<MsgInfo> result = msgInfoMapper.queryList(msgInfo);
        if (CollectionUtils.isEmpty(result)) {
            return;
        }

        for (MsgInfo msg : result) {
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(msg.getId());
            rabbitTemplate.convertAndSend("orderExchange", "orderRouting", JSONObject.toJSONString(msg), correlationData);
        }

    }

//    @Scheduled(cron = "0/1 * * * * ?")
    private void createOrder() {

        System.out.println("createOrder========" + Thread.currentThread().getName());

        MsgInfo msgInfo = new MsgInfo();
        String uuid = String.valueOf(UUID.randomUUID());
        msgInfo.setId(uuid);
        msgInfo.setAtime(new Date());
        msgInfo.setBizType("order");
        msgInfo.setTryCount(0);
        msgInfo.setMsgDesc("01");
        msgInfoMapper.insertSelective(msgInfo);

    }

}
