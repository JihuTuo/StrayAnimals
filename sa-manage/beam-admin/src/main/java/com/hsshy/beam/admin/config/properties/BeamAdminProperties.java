package com.hsshy.beam.admin.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 项目相关配置
 *
 * @author fengshuonan
 * @date 2017年10月23日16:44:15
 */
@Configuration
@ConfigurationProperties(prefix = BeamAdminProperties.BEAM_REST_PREFIX)
public class BeamAdminProperties {

    public static final String BEAM_REST_PREFIX = "beam.admin";

    private Boolean captchaOpen = false;

    private Boolean swaggerOpen = false;

    public Boolean getCaptchaOpen() {
        return captchaOpen;
    }

    public void setCaptchaOpen(Boolean captchaOpen) {
        this.captchaOpen = captchaOpen;
    }

    public Boolean getSwaggerOpen() {
        return swaggerOpen;
    }

    public void setSwaggerOpen(Boolean swaggerOpen) {
        this.swaggerOpen = swaggerOpen;
    }
}
