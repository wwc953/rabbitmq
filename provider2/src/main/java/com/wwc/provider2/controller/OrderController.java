package com.wwc.provider2.controller;

import com.wwc.provider2.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/createOrder")
    public void createOrder() {
        orderService.createOrder();
    }

}
