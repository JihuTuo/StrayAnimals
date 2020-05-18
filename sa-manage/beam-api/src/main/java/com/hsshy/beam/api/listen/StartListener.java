package com.hsshy.beam.api.listen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class StartListener implements ApplicationListener<ContextRefreshedEvent> {

    protected Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {


        logger.info("项目启动成功---------------------------------------------------");
    }
}
