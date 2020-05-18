package com.hsshy.beam.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;

import static com.hsshy.beam.common.utils.ToolUtil.getTempPath;
import static com.hsshy.beam.common.utils.ToolUtil.isEmpty;

@Data
@Configuration
@ConfigurationProperties(prefix = BeamRestProperties.BEAM_REST_PREFIX)
public class BeamRestProperties {

    public static final String BEAM_REST_PREFIX = "beam.rest";

    private boolean authOpen = true;

    private boolean signOpen = true;

    private String fileUploadPath;

    private Boolean haveCreatePath = false;

    private String secret;



    public String getFileUploadPath() {
        //如果没有写文件上传路径,保存到临时目录
        if (isEmpty(fileUploadPath)) {
            return getTempPath();
        } else {
            //判断有没有结尾符,没有得加上
            if (!fileUploadPath.endsWith(File.separator)) {
                fileUploadPath = fileUploadPath + File.separator;
            }
            //判断目录存不存在,不存在得加上
            if (!haveCreatePath) {
                File file = new File(fileUploadPath);
                file.mkdirs();
                haveCreatePath = true;
            }
            return fileUploadPath;
        }
    }

    public void setFileUploadPath(String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }
}
