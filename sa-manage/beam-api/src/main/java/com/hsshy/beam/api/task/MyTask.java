package com.hsshy.beam.api.task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 定时器
 */
//@Profile({"prod-8081"})
@Component
public class MyTask {

    protected Logger logger = LoggerFactory.getLogger(MyTask.class);



    /**
     * 每118分钟任务
     */
    @Scheduled(fixedRate = 1000*60*118)
    public void task(){
        logger.info("--------------定时任务--------------");

    }




}
