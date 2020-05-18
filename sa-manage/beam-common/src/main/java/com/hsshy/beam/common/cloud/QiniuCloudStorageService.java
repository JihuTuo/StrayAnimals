package com.hsshy.beam.common.cloud;
import com.hsshy.beam.common.exception.BeamException;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;


public class QiniuCloudStorageService extends CloudStorageService {
    private UploadManager uploadManager;
    private BucketManager bucketManager;
    private String token;
    private Auth auth;

    public QiniuCloudStorageService(CloudStorageConfig config) {
        this.config = config;
        //初始化
        init();
    }

    private void init() {
        auth = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey());
        Configuration c = new Configuration(Zone.autoZone());
        uploadManager = new UploadManager(c);
        bucketManager = new BucketManager(auth, c);
        token = auth.uploadToken(config.getQiniuBucketName());
    }


    public String upload(byte[] data, String path) {
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.toString());
            }
        } catch (Exception e) {
            throw new BeamException("上传文件失败，请核对七牛配置信息", e);
        }

        return config.getQiniuDomain() + "/" + path;
    }


    @Override
    public String uploadSuffix(String pic, String suffix) throws Exception {
        if (!StringUtils.isBlank(pic) && !pic.contains("http:") && !pic.contains("https:") && !pic.contains("upload")) {
            if (pic.indexOf("data:image/jpeg;base64") > -1 || pic.indexOf("data:image/png;base64") > -1) {
                pic = pic.substring(pic.indexOf(",") * 1 + 1, pic.length());
            }
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] data = decoder.decode(pic);
            for (int i = 0; i < data.length; ++i) {
                if (data[i] < 0) {
                    data[i] += 256;
                }
            }
//            InputStream input = new ByteArrayInputStream(data);
//            OutputStream output = new ByteArrayOutputStream();
//            //图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
//            Thumbnails.of(input).scale(1f).outputQuality(0.25f).toOutputStream(output);
            return upload(data, getPath(config.getQiniuPrefix(), suffix));
        } else {
            return pic;
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getQiniuPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return upload(data, getPath(config.getQiniuPrefix(), suffix));
        } catch (IOException e) {
            throw new BeamException("上传文件失败", e);
        }
    }

    @Override
    public void delete(String path) {
        if (path.contains(config.getQiniuDomain())) {
            String key = path.replaceAll(config.getQiniuDomain() + "/", "");
            try {
                //调用delete方法移动文件
                bucketManager.delete(config.getQiniuBucketName(), key);
            } catch (QiniuException e) {
                //捕获异常信息
                Response r = e.response;
                System.out.println(r.toString());
            }
        }
    }
}
