package com.hsshy.beam.api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@SpringBootApplication(scanBasePackages = {"com.hsshy.beam"})
@EnableSwagger2
@EnableScheduling
@EnableCaching
public class BeamApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeamApiApplication.class, args);
    }
}
