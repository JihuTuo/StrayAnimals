package com.shiep;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement   // 表示启用事务控制
@MapperScan("com.shiep.mapper")   // 配置MyBatisPlus mapper扫描
public class StaryAnimalsApplication {  // 使用本地tomcat部署 需要集成该类
    public static void main(String[] args) {
        SpringApplication.run(StaryAnimalsApplication.class);
    }


}
