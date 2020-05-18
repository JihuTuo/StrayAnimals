package com.shiep.controller;

import com.shiep.entity.PhotoAnimalAdopt;
import com.shiep.entity.PhotoAnimalSearch;
import com.shiep.service.IUploadPhotoService;
import com.shiep.util.FileUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("upload")
public class UploadFileController {
    @Resource
    private IUploadPhotoService uploadPhotoService;

    @PostMapping("adopt")
    public ResponseEntity<Void> uploadAdopt(@RequestParam("file")MultipartFile file,
                                            Long animalAdoptId, HttpServletRequest request) {
        String contentType = file.getContentType(); // 图片文件类型
        String fileName =	file.getOriginalFilename();	 // 图片名字
        System.out.println(animalAdoptId + "adopt-----"+ contentType + fileName + animalAdoptId) ;
        UUID uuid=UUID.randomUUID();
        // 文件名称
        String newfileName = uuid.toString()+fileName.substring(fileName.indexOf('.')); //文件重命名
        // 保存地址
        String filePathInLocal = "D:\\Tool_Workspace\\IDEA\\sa-front\\" + "upload\\adopt\\";
        String filePathInDb = "http:\\\\www.sa.com\\" + "upload\\adopt\\";
        // 上传处理
        boolean flag = false;
        try {
            FileUtil.uploadFile(file.getBytes(), filePathInLocal, newfileName);//文件处理
            // 信息保存到数据库
            PhotoAnimalAdopt photo = new PhotoAnimalAdopt();
            photo.setName(newfileName);
            photo.setPhoto(filePathInDb + newfileName);
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
        // 保存地址
        //String filePath = "//upload//adopt//";
        String filePathInLocal = "D:\\Tool_Workspace\\IDEA\\sa-front\\" + "upload\\search\\";
        String filePathInDb = "http:\\\\www.sa.com\\" + "upload\\search\\";
        // 上传处理
        boolean flag = false;
        try {
            FileUtil.uploadFile(file.getBytes(), filePathInLocal, newfileName);//文件处理
            // 信息保存到数据库
            PhotoAnimalSearch photo = new PhotoAnimalSearch();
            photo.setName(newfileName);
            photo.setPhoto(filePathInDb + newfileName);
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
