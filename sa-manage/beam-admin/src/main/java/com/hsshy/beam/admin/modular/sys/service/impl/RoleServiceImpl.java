package com.hsshy.beam.admin.modular.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsshy.beam.admin.modular.sys.dao.RoleMapper;
import com.hsshy.beam.admin.modular.sys.entity.Role;
import com.hsshy.beam.admin.modular.sys.service.IRoleService;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.redis.RedisUtil;
import com.hsshy.beam.common.utils.ToolUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 角色
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-10-10 21:13:03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public IPage<Role> selectPageList(Role role) {
        return baseMapper.selectPageList(new Page(role.getCurrentPage(),role.getPageSize()),role);
    }

    @Override
    public R deleteRole(Long[] roleIds) {
        if(ToolUtil.isEmpty(roleIds)||roleIds.length<=0){
            return R.fail("未选择删除的角色");
        }


        for(Long roleId:roleIds){
            Integer count = baseMapper.getCountByRoleId(roleId);
            if(count>0){
                return R.fail("当前删除的角色，还有用户关联，请先取消其关联");
            }
        }

        this.removeByIds(Arrays.asList(roleIds));
        return R.ok();
    }

    @Override
    public List<Long> getCheckMenuIds(Long roleId) {
        return baseMapper.getCheckMenuIds(roleId);
    }

    @Override
    public R saveMuenPerms(Role role) {
        Role r = this.getById(role.getId());
        if(ToolUtil.isEmpty(r)){
            return R.fail("找不到该角色");
        }
        baseMapper.delMenuPermByRoleId(role.getId());
        //清除缓存
        redisUtil.clearCache();

        if(role.getMenuIds().length<=0){

            return R.ok();
        }
        baseMapper.saveMenuPerms(role);

        //删除缓存

        return R.ok();




    }


}
