package com.shiep.service;

import com.shiep.entity.AdminCategory;
import com.shiep.entity.User;
import org.apache.ibatis.annotations.Update;

import java.beans.Transient;
import java.util.List;

public interface IUserService {

    User findByNameAndPwd(User user);

    User findByPhoneAndPwd(User user);

    User queryUserByUsername(String username);

    User getUserById(Long userId);

    @Transient
    boolean register(User user);

    User queryByEmail(String email);

    User queryUserByNameAndEmail(String username, String email);

    @Transient
    boolean updateUserById(User updateUser);

    @Transient
    boolean updatePhotoById(Long userId, String photo);

    @Transient
    boolean updatePwd(Long userId, String oldPwd, String newPwd, String email);

    List<User> QueryAllUser();

    @Transient
    boolean addUser(User addUser);

    @Transient
    boolean deleteUserById(Long userId);

    @Transient
    boolean deleteMultipleUsersByIds(List<Long> ids);

    List<User> queryUserByLike(String key, String value);

    @Transient
    boolean setAdminByIds(List<Long> ids, Integer roldId);

    List<AdminCategory> queryAllRoles();

    @Transient
    boolean rollbackMultipleUsersByIds(List<Long> ids);
}
