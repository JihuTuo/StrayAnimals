package com.hsshy.beam.admin.modular.sys.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hsshy.beam.admin.modular.sys.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 管理员表
 * 
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-10-07 18:03:20
 */
public interface UserMapper extends BaseMapper<User> {

    IPage<Map> selectPageList(Page page, @Param("user") User user);

    /**
     * 查询用户的所有权限
     * @param userId  用户ID
     */
    List<String> queryAllPerms(@Param("userId") Long userId, @Param("type") Integer type);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(@Param("userId") Long userId);


    List<Long> getRoleIdsById(@Param("userId") Long userId);

    void saveUserRole(@Param("user") User user);

    void delURByUserId(Long userId);

    void delURInUserId(@Param("userIds") Long userIds[]);

}
