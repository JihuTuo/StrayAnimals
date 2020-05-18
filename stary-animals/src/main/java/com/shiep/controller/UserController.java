package com.shiep.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiep.entity.AdminCategory;
import com.shiep.entity.PhotoAnimalAdopt;
import com.shiep.entity.User;
import com.shiep.mapper.IUserMapper;
import com.shiep.service.IUserService;
import com.shiep.util.FileUtil;
import com.shiep.vo.AnimalAdoptVo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("user")
public class UserController {
    @Resource
    private IUserMapper userMapper;

    @Resource
    private IUserService userService;

    @GetMapping("getUserById")
    public ResponseEntity<User> getUserById(@RequestParam("userId") Long userid) {
        User user = this.userService.getUserById(userid);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("updateUserById")
    public ResponseEntity<Void> updateUserById(User updateUser) {
        if (this.userService.updateUserById(updateUser)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("updatePhoto")
    public ResponseEntity<Void> updatePhotoById(@RequestParam("userId")Long userId,
                                                @RequestParam("file") MultipartFile file) {

        String contentType = file.getContentType(); // 图片文件类型
        String fileName =	file.getOriginalFilename();	 // 图片名字
        System.out.println( "adopt-----"+ contentType + fileName + userId) ;
        UUID uuid=UUID.randomUUID();
        // 文件名称
        String newfileName = uuid.toString()+fileName.substring(fileName.indexOf('.')); //文件重命名
        // 保存地址
        String filePathInlocal = "D:\\Tool_Workspace\\IDEA\\sa-front\\" + "upload\\user\\";
        String filePathInDb = "http:\\\\www.sa.com\\" + "upload\\user\\";
        // 上传处理
        boolean flag = false;
        try {
            FileUtil.uploadFile(file.getBytes(), filePathInlocal, newfileName);//文件处理
            // 信息保存到数据库
            flag = this.userService.updatePhotoById(userId, filePathInDb + newfileName);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.badRequest().build();
        }
        if (!flag) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("updatePwdById")
    public ResponseEntity<Void> updatePwdById(@RequestParam("userId")Long userId, @RequestParam("oldPwd")String oldPwd,
                        @RequestParam("newPwd")String newPwd, @RequestParam("email")String email) {
        boolean pwd = this.userService.updatePwd(userId, oldPwd, newPwd, email);
        if (pwd) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("queryAllUserInPage")
    public ResponseEntity<PageInfo<User>> queryAllAnimalAdoptInPage(
            int size, int page) {
        Page p = PageHelper.startPage(page, size);
        List<User> users = this.userService.QueryAllUser();

        PageInfo<User> pageInfo = new PageInfo(users);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }

    @PostMapping("addUser")
    public ResponseEntity<Void> addUser(User addUser) {
        boolean add = this.userService.addUser(addUser);
        if (add) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("deleteUserById")
    public ResponseEntity<Void> deleteUserById(@RequestParam("userId")Long userId) {
        boolean delete = this.userService.deleteUserById(userId);
        if(delete) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("deleteMultipleUsersByIds")
    public ResponseEntity<Void> deleteMultipleUsersByIds(@RequestParam("ids") List<Long> ids) {
       if (this.userService.deleteMultipleUsersByIds(ids)) {
           return ResponseEntity.ok().build();
       }
       return ResponseEntity.notFound().build();
    }

    @PostMapping("rollbackMultipleUsersByIds")
    public ResponseEntity<Void> rollbackMultipleUsersByIds(@RequestParam("ids") List<Long> ids) {
        if (this.userService.rollbackMultipleUsersByIds(ids)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("likeSearch")
    public ResponseEntity<PageInfo<User>> likeSearch(
            @RequestParam(value = "key", required = false)String key, @RequestParam(value = "value", required = false)String value,
            @RequestParam("page")Integer page, @RequestParam("size")Integer size) {
        Page p = PageHelper.startPage(page, size);

        List<User> users = this.userService.queryUserByLike(key, value);

        PageInfo<User> pageInfo = new PageInfo(users);

        pageInfo.setPages(p.getPages());//总页数
        pageInfo.setTotal(p.getTotal());//总条数

        return ResponseEntity.ok(pageInfo);
    }

    @GetMapping("setAdminByIds")
    public ResponseEntity<Void> setAdminByIds(@RequestParam("ids") List<Long> ids, @RequestParam("roleId")Integer roleId) {
        if (this.userService.setAdminByIds(ids, roleId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("queryAllRoles")
    public ResponseEntity<List<AdminCategory>> queryAllRoles() {
        List<AdminCategory> roles = this.userService.queryAllRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(roles);
    }
}
