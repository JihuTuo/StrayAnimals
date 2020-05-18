package com.shiep;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableTransactionManagement   // 表示启用事务控制
//@MapperScan({"com.shiep.mapper", "com.shiep.dao"})   // 配置MyBatisPlus mapper扫描
@MapperScan("com.shiep.mapper")   // 配置MyBatisPlus mapper扫描
public class SpringBootApplication {  // 使用本地tomcat部署 需要集成该类
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class);
    }
}
