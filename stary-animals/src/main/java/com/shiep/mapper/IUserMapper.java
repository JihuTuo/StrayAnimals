package com.shiep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiep.entity.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.beans.Transient;
import java.util.List;

public interface IUserMapper extends BaseMapper<User> {
    @Update("UPDATE sa_user SET photo = #{photo} WHERE id = #{userId}")
    int updatePhotoById(Long userId, String photo);

    @Update("UPDATE sa_user SET password = #{newPwd} WHERE id = #{userId} AND email = #{email} AND password = #{oldPwd}")
    int updatePwd(Long userId, String oldPwd, String newPwd, String email);


    @Update("UPDATE sa_user SET delete_status = 1 WHERE id = #{userId}")
    int deleteUserById(Long userId);

    @Update("UPDATE sa_user SET sa_admin_category_id = #{roleId} WHERE id = #{id}")
    int setAdminById(Long id, Integer roleId);

    @Update("UPDATE sa_user SET delete_status = 0 WHERE id = #{userId}")
    int rollbackUserById(Long userId);
}
