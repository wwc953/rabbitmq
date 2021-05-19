package com.wwc.provider2.config;

import com.wwc.provider2.bean.MsgInfo;
import com.wwc.provider2.dao.MsgInfoMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitCheckConfig {

    @Autowired
    MsgInfoMapper msgInfoMapper;

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        //确认消息是否到达exchange
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("ConfirmCallback:     " + "相关数据：" + correlationData);
                if (ack) {
                    System.out.println("消息到达rabbitmq服务器");
                    MsgInfo msgInfo = new MsgInfo();
                    msgInfo.setId(correlationData.getId());
                    msgInfo.setMsgDesc("02");
                    msgInfoMapper.updateByPrimaryKeySelective(msgInfo);
                } else {
                    System.out.println("消息可能未到达rabbitmq服务器");

                }
            }
        });

        //消息没有正确到达队列时触发回调，如果正确到达队列不执行
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("ReturnCallback:     " + "消息：" + message);
                System.out.println("ReturnCallback:     " + "回应码：" + replyCode);
                System.out.println("ReturnCallback:     " + "回应信息：" + replyText);
                System.out.println("ReturnCallback:     " + "交换机：" + exchange);
                System.out.println("ReturnCallback:     " + "路由键：" + routingKey);
            }
        });

        return rabbitTemplate;
    }

    @Bean
    public Queue TestDirectQueue() {
        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue("OrderQueue", true);
    }

    @Bean
    DirectExchange orderExchange() {
        return new DirectExchange("orderExchange", true, false);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(TestDirectQueue()).to(orderExchange()).with("orderRouting");
    }


}
