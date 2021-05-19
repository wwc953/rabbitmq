package com.wwc.consumer2.ack;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.wwc.consumer2.bean.MsgInfo;
import com.wwc.consumer2.dao.MsgInfoMapper;
import com.wwc.consumer2.utils.Constant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyAckReceiver implements ChannelAwareMessageListener {

    @Autowired
    MsgInfoMapper msgInfoMapper;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String msg = message.toString();
        MsgInfo msgobj = JSONObject.parseObject(message.getBody(), MsgInfo.class);

        try {
            if (Constant.order_queue.equals(message.getMessageProperties().getConsumerQueue())) {
                System.out.println("消息成功消费到  messageId:" + msg);
                System.out.println("执行TestDirectQueue中的消息的业务处理流程......");

                MsgInfo almsgobj = msgInfoMapper.selectByPrimaryKey(msgobj.getId());
                if (almsgobj != null) {
                    System.out.println("重复消息:" + msg);
                    almsgobj.setAtime(new Date());
                    msgInfoMapper.updateByPrimaryKeySelective(almsgobj);
                } else {
                    msgobj.setAtime(new Date());
                    msgInfoMapper.insertSelective(msgobj);
                }
            }
            //第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            channel.basicReject(deliveryTag, true);

//            if (message.getMessageProperties().getRedelivered()) {
//                System.out.println("消息已重复处理失败,拒绝再次接收");
//                // 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列
//                channel.basicReject(deliveryTag, false);
//            } else {
//                System.out.println("消息即将再次返回队列处理");
//                // requeue为是否重新回到队列，true重新入队
//                channel.basicNack(deliveryTag, false, true);
//            }

//            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
        }
    }

}
