package com.shiep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiep.entity.AdminCategory;
import com.shiep.mapper.IAdminCategoryMapper;
import com.shiep.mapper.IUserMapper;
import com.shiep.entity.User;
import com.shiep.service.IUserService;
import com.shiep.util.CodecUtils;
import com.shiep.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.beans.Transient;
import java.util.List;


@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private IAdminCategoryMapper adminCategoryMapper;

    public User findByNameAndPwd(User user) {
        /*m'ybatis 3 以上的版本是QueryWrapper，之前的是EntityWrapper*/
        if (StringUtils.isNotBlank(user.getUsername()) && StringUtils.isNotBlank(user.getPassword())) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword()).eq(User::getDeleteStatus, 0);
            User userInfo = this.userMapper.selectOne(queryWrapper);
            return userInfo;
        }
        return null;
    }

    public User findByNameAndPwd(String username, String password) {
        /*m'ybatis 3 以上的版本是QueryWrapper，之前的是EntityWrapper*/
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getUsername, username).eq(User::getPassword, password).eq(User::getDeleteStatus, 0);
            User user = this.userMapper.selectOne(queryWrapper);
            return user;
        }
        return null;
    }

    // 这里phone存在username中
    public User findByPhoneAndPwd(User user) {
        if (StringUtils.isNotBlank(user.getUsername()) && StringUtils.isNotBlank(user.getPassword())) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            String phone = user.getUsername();   // 这里phone其实存在username里面
            queryWrapper.lambda().eq(User::getPhone, phone).eq(User::getPassword, user.getPassword()).eq(User::getDeleteStatus, 0);
            User userInfo = this.userMapper.selectOne(queryWrapper);
            return userInfo;
        }
        return null;
    }

    @Override
    public User queryUserByUsername(String username) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getUsername, username).eq(User::getDeleteStatus, 0);
        return this.userMapper.selectOne(qw);
    }

    @Override
    public User getUserById(Long userId) {
        if (userId == null) {
            return null;
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getDeleteStatus, 0).eq(User::getId, userId);
        User user = this.userMapper.selectOne(qw);
        if (user == null) {
            return null;
        }
        return user;
    }

    @Override
    public boolean register(User user) {
        if (user == null) {
            return  false;
        }
        // 注意，用户名和邮箱不允许重复！！！！！！！！！！！！
        QueryWrapper<User> qwName = new QueryWrapper<>();
        qwName.lambda().eq(User::getUsername, user.getUsername());
        if (this.userMapper.selectCount(qwName) > 0) {
            return false;
        }

        QueryWrapper<User> qwEmail = new QueryWrapper<>();
        qwEmail.lambda().eq(User::getEmail, user.getEmail());
        if (this.userMapper.selectCount(qwEmail) > 0) {
            return false;
        }

        // 生成盐
        String salt = CodecUtils.generateSalt();
        User newUser = new User();

        newUser.setSalt(salt);
        // 对密码进行MD5+salt 加密
        newUser.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        // 新增用户
        newUser.setId(null);  // 防止注入
        newUser.setUsername(user.getUsername());
        newUser.setCreateTime(user.getCreateTime());
        newUser.setEmail(user.getEmail());
        newUser.setEmail(user.getEmail());
        newUser.setCreateTime(user.getCreateTime());


        int insert = this.userMapper.insert(newUser);

        // 删除缓存
        //this.redisUtil.del(user.getEmail());

        if (insert > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User queryByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return null;
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getEmail, email);
        User user = this.userMapper.selectOne(qw);
        return user;
    }

    @Override
    public User queryUserByNameAndEmail(String username, String email) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(email)) {
            return null;
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getDeleteStatus, 0).eq(User::getUsername, username).eq(User::getEmail, email);
        User user = this.userMapper.selectOne(qw);
        return user;
    }

    @Transient
    @Override
    public boolean updateUserById(User updateUser) {
        if (updateUser == null) {
            return false;
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getId, updateUser.getId());
        int update = this.userMapper.update(updateUser, qw);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Transient
    @Override
    public boolean updatePhotoById(Long userId, String photo) {
        if (userId == null || StringUtils.isBlank(photo)) {
            return false;
        }
        int update = this.userMapper.updatePhotoById(userId, photo);

        if (update > 0) {
            return true;

        }
        return false;
    }

    @Override
    public boolean updatePwd(Long userId, String oldPwd, String newPwd, String email) {
        if (userId == null || StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd) ||
                StringUtils.isBlank(email)) {
            return false;
        }
        // 首先根据userId获取用户信息
        User user = this.userMapper.selectById(userId);
        // 邮箱 如果邮箱不一致，直接返回错
        if (!StringUtils.equals(email, user.getEmail())) {
            return false;
        }
        // 根据盐来生成密码 与数据库中比较
        String pwd = CodecUtils.md5Hex(oldPwd, user.getSalt());
        if (!StringUtils.equals(pwd, user.getPassword())) {
            return false;
        }

        // 此时还要对新密码进行MD5加密处理, 此时盐就使用原来的吧
        String pwdInDb = CodecUtils.md5Hex(newPwd, user.getSalt());


        // 如果都想等，进行修改
        int updatePwd = this.userMapper.updatePwd(userId, pwd, pwdInDb, email);
        if (updatePwd > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<User> QueryAllUser() {
        List<User> users = this.userMapper.selectList(null);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        return users;
    }

    @Override
    public boolean addUser(User addUser) {
        if (addUser == null) {
            return false;
        }
        // 注意，用户名和邮箱不允许重复！！！！！！！！！！！！
        QueryWrapper<User> qwName = new QueryWrapper<>();
        qwName.lambda().eq(User::getUsername, addUser.getUsername());
        if (this.userMapper.selectCount(qwName) > 0) {
            return false;
        }

        QueryWrapper<User> qwEmail = new QueryWrapper<>();
        qwEmail.lambda().eq(User::getEmail, addUser.getEmail());
        if (this.userMapper.selectCount(qwEmail) > 0) {
            return false;
        }

        // 生成盐
        String salt = CodecUtils.generateSalt();
        User newUser = new User();

        newUser.setSalt(salt);
        // 对密码进行MD5+salt 加密
        newUser.setPassword(CodecUtils.md5Hex(addUser.getPassword(), salt));

        // 新增用户
        newUser.setId(null);  // 防止注入
        newUser.setUsername(addUser.getUsername());
        newUser.setCreateTime(addUser.getCreateTime());
        newUser.setEmail(addUser.getEmail());
        newUser.setCreateTime(addUser.getCreateTime());

        int insert = 0;
        try {
            insert = this.userMapper.insert(newUser);
        } catch (Exception e) {
            return false;
        }

        // 删除缓存
        //this.redisUtil.del(user.getEmail());

        if (insert > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteUserById(Long userId) {
        if (userId == null) {
            return false;
        }
        return this.userMapper.deleteUserById(userId) > 0;
    }

    @Override
    public boolean deleteMultipleUsersByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
       try {
           for (Long id : ids) {
               int delete = this.userMapper.deleteUserById(id);
           }
       } catch (Exception e) {
           return false;
       }
        return true;
    }

    @Override
    public boolean rollbackMultipleUsersByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        try {
            for (Long id : ids) {
                int delete = this.userMapper.rollbackUserById(id);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<User> queryUserByLike(String key, String value) {
        if (StringUtils.isBlank(value)) {
            List<User> users = this.userMapper.selectList(null);
            return users;
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.like(key, value);
        List<User> users = this.userMapper.selectList(qw);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        return users;
    }

    @Override
    public boolean setAdminByIds(List<Long> ids, Integer roleId) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        try {
            for (Long id : ids) {
                this.userMapper.setAdminById(id, roleId);
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public List<AdminCategory> queryAllRoles() {
        List<AdminCategory> roles = this.adminCategoryMapper.selectList(null);
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }
        return roles;
    }
}
