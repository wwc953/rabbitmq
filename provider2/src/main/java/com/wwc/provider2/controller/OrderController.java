package com.wwc.provider2.controller;

import com.alibaba.fastjson.JSONObject;
import com.wwc.provider2.bean.MsgInfo;
import com.wwc.provider2.service.OrderService;
import com.wwc.provider2.utils.Constant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/createOrder")
    public void createOrder() {
        orderService.createOrder();
    }

    @GetMapping("/createDead")
    public void createDead() {
        String uuid = String.valueOf(UUID.randomUUID());
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setId(uuid);
        msgInfo.setMsgDesc("消息西峡县");

        CorrelationData correlationData = new CorrelationData(uuid);
        /**
         * 发送给普通对队列,设置五秒之后过期
         * 但是并没有实现消费的监听，因此该消息将在五秒之后过期
         */
        rabbitTemplate.convertAndSend(Constant.order_exchange, Constant.order_rounting, JSONObject.toJSONString(msgInfo), message -> {
            message.getMessageProperties().setExpiration("5000");
            return message;
        }, correlationData);
    }

}
