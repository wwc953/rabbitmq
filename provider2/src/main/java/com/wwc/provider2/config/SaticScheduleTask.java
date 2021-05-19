package com.wwc.provider2.config;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wwc.provider2.bean.MsgInfo;
import com.wwc.provider2.dao.MsgInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    @Autowired
    MsgInfoMapper msgInfoMapper;

    //3.添加定时任务
    @Scheduled(cron = "0/3 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
//        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());

        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setMsgDesc("01");
        msgInfo.setBizType("order");
        List<MsgInfo> result = msgInfoMapper.queryList(msgInfo);
        System.out.println(JSONObject.toJSONString(result));

    }

}
