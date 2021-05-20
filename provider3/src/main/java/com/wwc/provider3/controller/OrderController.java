package com.wwc.provider3.controller;

import com.wwc.provider3.config.RabbitDelayConfig;
import com.wwc.provider3.service.RabbitDelaySender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {

    @Autowired
    RabbitDelaySender sender;

    @GetMapping("/createDead/{msg}")
    public void createDead(@PathVariable String msg) {
        sender.sendMsg(RabbitDelayConfig.DELAY_TEST_EXCHANGE, RabbitDelayConfig.DELAY_TEST_ROUTING_KEY,
                msg, null);
    }

}
