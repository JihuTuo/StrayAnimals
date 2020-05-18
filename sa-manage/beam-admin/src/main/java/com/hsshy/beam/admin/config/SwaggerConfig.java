package com.hsshy.beam.admin.config;
import com.hsshy.beam.admin.config.properties.BeamAdminProperties;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置类
 *
 * @author hs
 * @date 2018年9月19日19:42:59
 */
@Configuration
@EnableSwagger2
//从application.yml 取前缀为beam name为 swagger-open 值为true时 configuration生效
@ConditionalOnProperty(prefix = BeamAdminProperties.BEAM_REST_PREFIX, name = "swagger-open", havingValue = "true",matchIfMissing = false)
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))                         //这里采用包含注解的方式来确定要显示的接口
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Beam Doc")
                .description("Beam Api文档")
                .termsOfServiceUrl("暂无")
                .version("2.0")
                .build();
    }



}
