package com.hsshy.beam.api.modular.common;

import com.hsshy.beam.common.annotion.IgnoreUTokenAuth;
import com.hsshy.beam.common.constant.RetEnum;
import com.hsshy.beam.common.exception.BeamException;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.ToolUtil;
import com.hsshy.beam.web.modular.common.util.OSSFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Api(value = "FileController", tags = {"文件上传接口"})
@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * 文件上传
     */
    @ApiOperation(value = "上传缩略图")
    @IgnoreUTokenAuth
    @RequestMapping(value = "/upload/image")
    public R uploadThumbImg(@RequestPart("file") MultipartFile file) {
        System.out.println("图片大小："+file.getSize());
        String suffix = "." + ToolUtil.getFileSuffix(file.getOriginalFilename());
        try {
            if (file.getSize() >= 2 * 1024 * 1024) {
                return R.fail("图片大小不能超过2M");
            } else if (file.getSize() >= 150 * 1024) {
//                System.out.println("进行图片压缩");
                byte[] data = file.getBytes();
                InputStream input = new ByteArrayInputStream(data);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                //图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
                Thumbnails.of(input).scale(1f).outputQuality(0.8f).toOutputStream(output);
                byte[] result = output.toByteArray();
                String url = OSSFactory.build().uploadSuffix(result, suffix);
                return R.ok(url);
            } else {
                String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);
                return R.ok(url);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BeamException(RetEnum.UPLOAD_ERROR);
        }

    }


}