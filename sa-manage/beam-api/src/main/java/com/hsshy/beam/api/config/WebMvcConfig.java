package com.hsshy.beam.api.config;

import com.hsshy.beam.api.config.properties.BeamRestProperties;
import com.hsshy.beam.api.interceptors.AppInterceptor;
import com.hsshy.beam.api.sign.converter.WithSignMessageConverter;
import com.hsshy.beam.api.sign.security.DataSecurityAction;
import com.hsshy.beam.api.sign.security.impl.Base64SecurityAction;
import com.hsshy.beam.common.config.DefaultFastjsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AppInterceptor appInterceptor;

    @ConditionalOnProperty(prefix = BeamRestProperties.BEAM_REST_PREFIX, name = "auth-open", havingValue = "true", matchIfMissing = true)
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(appInterceptor).addPathPatterns("/**");
    }



    @Bean
    @ConditionalOnProperty(prefix = BeamRestProperties.BEAM_REST_PREFIX, name = "sign-open", havingValue = "true", matchIfMissing = true)
    public WithSignMessageConverter withSignMessageConverter() {
        WithSignMessageConverter withSignMessageConverter = new WithSignMessageConverter();
        DefaultFastjsonConfig defaultFastjsonConfig = new DefaultFastjsonConfig();
        withSignMessageConverter.setFastJsonConfig(defaultFastjsonConfig.fastjsonConfig());
        withSignMessageConverter.setSupportedMediaTypes(defaultFastjsonConfig.getSupportedMediaType());
        return withSignMessageConverter;
    }

    @Bean
    public DataSecurityAction dataSecurityAction() {
        return new Base64SecurityAction();
    }


}