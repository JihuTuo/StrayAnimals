package com.shiep.controller;

import com.shiep.common.ueditor.ActionEnter;
import com.shiep.entity.PhotoAnimalSearch;
import com.shiep.service.IUploadPhotoService;
import com.shiep.util.FileUtil;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("ueditor")
@CrossOrigin(allowCredentials = "true")
public class UEditorController {

    private static final String DESK_PATH = System.getProperty("user.dir") + File.separator + "stary-animals-ui" + File.separator + "upload"
            + File.separator + "ueditor" + File.separator;

    private static final String FILE_PATH = "upload"
            + File.separator + "ueditor" + File.separator;

    @Autowired
    private IUploadPhotoService uploadPhotoService;

    /**
     * 上传配置：即不走config.json，模拟config.json里的内容，解决后端配置项不正确，无法上传的问题
     *
     * @return
     */
    @RequestMapping("config")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
   public void config(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        System.out.println("===== action=" + request.getParameter("action")
                + "id" + request.getParameter("id") + "     type=" + request.getParameter("type")
                + "     upfile=" + request.getParameter("upfile")) ;


        String rootPath = request.getSession().getServletContext().getRealPath("/");

        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            System.out.println(exec + "exec") ;
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
       /* MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile files = multipartRequest.getFile("upfile");
        System.out.println("MultipartFile=" +files) ;*/
    }
   /*public String uploadConfig(HttpServletRequest request) {
        String callback =request.getParameter("callback");


        String s = "{\n" +
                "            \"imageActionName\": \"uploadimage\",\n" +
                "                \"imageFieldName\": \"upfile\", \n" +
                "                \"imageMaxSize\": 2048000, \n" +
                "                \"imageAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"], \n" +
                "                \"imageCompressEnable\": true, \n" +
                "                \"imageCompressBorder\": 1600, \n" +
                "                \"imageInsertAlign\": \"none\", \n" +
                "                \"imageUrlPrefix\": \"\",\n" +
                "                \"imagePathFormat\": \"/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}\" }";

       // JSONObject json = JSON.parseObject(s);
        return callback + s;
        //return json;
    }*/



    @PostMapping("upload")
    public Map<String, String> uploadSearch( MultipartFile upfile,
                                             Long reportId, HttpServletRequest request,
                                             HttpServletResponse response) throws UnsupportedEncodingException {
//        Assert.notNull(reportId, "reportId must not be null.");

        String contentType = upfile.getContentType(); // 图片文件类型
        String fileName =	upfile.getOriginalFilename();	 // 图片名字
        System.out.println(reportId + "adopt---==--"+ contentType + fileName + reportId) ;
        UUID uuid=UUID.randomUUID();
        // 文件名称
        String newfileName = uuid.toString()+fileName.substring(fileName.indexOf('.')); //文件重命名
        // 保存地址

        // 上传处理
        boolean flag = false;
        try {
            FileUtil.uploadFile(upfile.getBytes(), DESK_PATH, newfileName);//文件处理
            // 信息保存到数据库
            PhotoAnimalSearch photo = new PhotoAnimalSearch();
            photo.setName(newfileName);
            photo.setPhoto(FILE_PATH + newfileName);
            photo.setAnimalSearchId(reportId);
            flag = this.uploadPhotoService.saveSearchPhoto(photo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("文件上传成功：");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String rootPath = request.getRealPath("/");
        new ActionEnter( request, rootPath ).exec();


        // 根据官方接口文档，自定义上传接口要返回的格式如下
        Map<String, String> map = new HashMap<>();
        map.put("code", "200");
        map.put("state", "SUCCESS");
        map.put("url", FILE_PATH + newfileName);
        map.put("title", newfileName);
        map.put("original", fileName);

        System.out.println("url=   " + map.get("url")) ;
        System.out.println("filename=   " + map.get("original")) ;
        System.out.println("title=   " + map.get("title")) ;
        return map;
    }

}
