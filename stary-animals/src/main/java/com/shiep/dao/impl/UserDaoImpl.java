package com.shiep.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiep.dao.IUserDao;
import com.shiep.entity.User;
import com.shiep.mapper.IUserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserDaoImpl implements IUserDao {
    @Resource
    private IUserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        QueryWrapper<User> qwUser = new QueryWrapper<>();
        qwUser.lambda().eq(User::getId, id);
        User user = this.userMapper.selectOne(qwUser);
        if (user == null) {
            return  null;
        }
        return user;
    }
}
