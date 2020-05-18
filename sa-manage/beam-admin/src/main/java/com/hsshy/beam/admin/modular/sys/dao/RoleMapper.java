package com.hsshy.beam.admin.modular.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hsshy.beam.admin.modular.sys.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 角色
 * 
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-10-10 21:13:03
 */
public interface RoleMapper extends BaseMapper<Role> {

    IPage<Role> selectPageList(Page page, @Param("role") Role role);

    Integer getCountByRoleId(Long roleId);

    List<Long> getCheckMenuIds(Long roleId);

    void saveMenuPerms(@Param("role") Role role);

    void delMenuPermByRoleId(Long roleId);



}
