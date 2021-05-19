package com.wwc.provider2.service;

import com.wwc.provider2.bean.MsgInfo;
import com.wwc.provider2.bean.Order;
import com.wwc.provider2.dao.MsgInfoMapper;
import com.wwc.provider2.dao.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    MsgInfoMapper msgInfoMapper;

    @Transactional
    public void createOrder() {

        Order order = new Order();
        order.setFlag(1);
        order.setCreateTime(new Date());
        order.setUserId(666);
        order.setOrderId("order-id");
        orderMapper.insertSelective(order);

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
