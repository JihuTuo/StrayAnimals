package com.hsshy.beam.admin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//@EnableRedisHttpSession
@SpringBootApplication(scanBasePackages = "com.hsshy.beam")
public class BeamAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeamAdminApplication.class, args);
    }
}
