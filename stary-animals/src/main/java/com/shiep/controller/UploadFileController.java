package com.shiep.controller;

import com.shiep.entity.PhotoAnimalAdopt;
import com.shiep.entity.PhotoAnimalSearch;
import com.shiep.mapper.INewsMapper;
import com.shiep.mapper.IPhotoAnimalAdoptMapper;
import com.shiep.mapper.IPhotoAnimalSearchMapper;
import com.shiep.mapper.IReportMapper;
import com.shiep.service.INewsService;
import com.shiep.service.IUploadPhotoService;
import com.shiep.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

@Controller
@RequestMapping("upload")
public class UploadFileController {
    @Resource
    private IUploadPhotoService uploadPhotoService;

    @Resource
    private IReportMapper reportMapper;


    @Resource
    private IPhotoAnimalSearchMapper photoAnimalSearchMapper;



    private static final String ADOPT_PATH_SUFFIX = File.separator + "adopt" + File.separator;

    private static final String SEARCH_PATH_SUFFIX = File.separator + "search" + File.separator;

    private static String adoptFileUpload_path; // 文件服务器访问路径

    private static String adoptFileUploadDeskPath; // 文件上传磁盘路径

    private static String searchFileUploadPath;
    private static String searchFileUploadDeskPath;

    @EventListener(ApplicationReadyEvent.class)
    public void test() {
        reportMapper.findAll().forEach(report -> {
            String content = report.getContent();
            content = content.replace("http://www.sa.com", "");
//            photo = photo.replace("127.0.0.0:9001/", "");

            report.setContent(content);

            reportMapper.updateById(report);
        });
    }

    @PostConstruct
    public void init() {
        String fileUploadDeskPrefix = System.getProperty("user.dir") + File.separator + "stary-animals-ui" + File.separator + "upload";
        adoptFileUploadDeskPath = fileUploadDeskPrefix + ADOPT_PATH_SUFFIX;
        searchFileUploadDeskPath = fileUploadDeskPrefix + SEARCH_PATH_SUFFIX;

//        String fileUploadPrefix = "127.0.0.1:9001" + File.separator + "upload";
        String fileUploadPrefix = "upload";
        adoptFileUpload_path = fileUploadPrefix + ADOPT_PATH_SUFFIX;
        searchFileUploadPath = fileUploadPrefix + SEARCH_PATH_SUFFIX;
    }

    @PostMapping("adopt")
    public ResponseEntity<Void> uploadAdopt(@RequestParam("file")MultipartFile file,
                                            Long animalAdoptId, HttpServletRequest request) {
        String contentType = file.getContentType(); // 图片文件类型
        String fileName =	file.getOriginalFilename();	 // 图片名字
        System.out.println(animalAdoptId + "adopt-----"+ contentType + fileName + animalAdoptId) ;
        UUID uuid=UUID.randomUUID();
        // 文件名称
        String newfileName = uuid + fileName.substring(fileName.indexOf('.')); //文件重命名
        // 上传处理
        boolean flag = false;
        try {
            FileUtil.uploadFile(file.getBytes(), adoptFileUploadDeskPath, newfileName);//文件处理
            // 信息保存到数据库
            PhotoAnimalAdopt photo = new PhotoAnimalAdopt();
            photo.setName(newfileName);
            photo.setPhoto(adoptFileUpload_path + newfileName);
            photo.setAnimalAdoptId(animalAdoptId);
            flag = this.uploadPhotoService.saveAdoptPhoto(photo);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.badRequest().build();
        }
        if (!flag) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("search")
    public ResponseEntity<Void> uploadSearch(@RequestParam("file")MultipartFile file,
                                            Long animalSearchId, HttpServletRequest request) {
        String contentType = file.getContentType(); // 图片文件类型
        String fileName =	file.getOriginalFilename();	 // 图片名字
        System.out.println(animalSearchId + "adopt-----"+ contentType + fileName + animalSearchId) ;
        UUID uuid=UUID.randomUUID();
        // 文件名称
        String newfileName = uuid.toString()+fileName.substring(fileName.indexOf('.')); //文件重命名
        // 上传处理
        boolean flag = false;
        try {
            FileUtil.uploadFile(file.getBytes(), searchFileUploadDeskPath, newfileName);//文件处理
            // 信息保存到数据库
            PhotoAnimalSearch photo = new PhotoAnimalSearch();
            photo.setName(newfileName);
            photo.setPhoto(searchFileUploadPath + newfileName);
            photo.setAnimalSearchId(animalSearchId);
            flag = this.uploadPhotoService.saveSearchPhoto(photo);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.badRequest().build();
        }
        if (!flag) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
