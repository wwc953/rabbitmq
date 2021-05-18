package com.wwc.provider.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadFactory;

@RestController
public class CheckController {

    @Resource
    RabbitTemplate checkRabbitTemplate;

    /**
     * ①消息推送到server，但是在server里找不到交换机
     *
     * @return
     */
    @GetMapping("/TestMessageAck")
    public String TestMessageAck() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: non-existent-exchange test message ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        checkRabbitTemplate.convertAndSend("non-existent-exchange", "TestDirectRouting", map);
        return "ok";

    }

    /**
     * ②消息推送到server，找到交换机了，但是没找到队列
     *
     * @return
     */
    @GetMapping("/TestMessageAck2")
    public String TestMessageAck2() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: lonelyDirectExchange test message ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        checkRabbitTemplate.convertAndSend("lonelyDirectExchange222", "TestDirectRouting", map);
        return "ok";
    }


//    @GetMapping("/run/{aa}")
    public void run(@PathVariable int aa) {

        for (int i = 0; i < aa; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10000; j++) {
                        System.out.println(Thread.currentThread().getName() + "i");
                    }
                }
            }).start();
        }
    }
}
