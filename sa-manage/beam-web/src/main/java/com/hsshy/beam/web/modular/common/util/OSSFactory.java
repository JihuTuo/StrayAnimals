package com.hsshy.beam.web.modular.common.util;
import com.hsshy.beam.common.cloud.*;
import com.hsshy.beam.common.constant.Constant;
import com.hsshy.beam.common.utils.SpringContextHolder;
import com.hsshy.beam.web.modular.common.service.ISysConfigService;

/**
 * 文件上传Factory
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-26 10:18
 */
public final class OSSFactory {
    private static ISysConfigService sysConfigService;

    static {
        OSSFactory.sysConfigService = SpringContextHolder.getBean("sysConfigService");
    }

    public static CloudStorageService build(){
        //获取云存储配置信息
        CloudStorageConfig config = sysConfigService.getConfigObject(ConfigConstant.CLOUD_STORAGE_CONFIG_KEY, CloudStorageConfig.class);

        if(config.getType() == Constant.CloudService.QINIU.getValue()){
            return new QiniuCloudStorageService(config);
        }

        return null;
    }

}
