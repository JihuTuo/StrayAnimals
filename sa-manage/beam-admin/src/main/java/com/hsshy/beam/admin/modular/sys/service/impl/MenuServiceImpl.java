package com.hsshy.beam.admin.modular.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hsshy.beam.admin.modular.sys.dao.MenuMapper;
import com.hsshy.beam.admin.modular.sys.entity.Menu;
import com.hsshy.beam.admin.modular.sys.service.IMenuService;
import com.hsshy.beam.admin.modular.sys.service.IUserService;
import com.hsshy.beam.common.constant.Constant;
import com.hsshy.beam.common.constant.CacheKey;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.redis.RedisUtil;
import com.hsshy.beam.common.utils.ToolUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-10-08 16:33:17
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<Map> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<Map> menuList = queryListParentId(parentId);
        if(menuIdList == null){
            return menuList;
        }
        List<Map> userMenuList = new ArrayList<>();
        for(Map menu : menuList){
            if(menuIdList.contains(menu.get("id"))){
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    @Override
    public List<Map> queryListParentId(Long parentId) {
        return baseMapper.queryListParentId(parentId);
    }

    @Override
    public List<Map> treeMenuList(Long menuId, Menu menu) {
        List<Map> menuList ;
        if(ToolUtil.isNotEmpty(menu.getName())){
            QueryWrapper qw = new QueryWrapper<Map>();
            qw.like("name",menu.getName());
            menuList = this.list(qw);
        }
        else {
            menuList =  queryListParentId(menuId);
        }
        return getAllMenuTreeList(menuList);
    }

    @Transactional
    @Override
    public R deleteMenu(Long menuIds[]) {
        if(ToolUtil.isEmpty(menuIds)||menuIds.length<=0){
            return R.fail("未提交要删除的记录");
        }
        for(Long menuId:menuIds){
            Integer count = this.count(new QueryWrapper<Menu>().lambda().eq(Menu::getParentId,menuId));
            if(count>0){
                return R.fail("删除失败，请先删除菜单关联的子菜单");
            }

        }
        //清除缓存
        redisUtil.clearCache();
        Boolean a = this.removeByIds(Arrays.asList(menuIds));
        return R.ok();
    }


    @Override
    @Cacheable(value = CacheKey.CONSTANT, key = "'" + CacheKey.USER_ID + "'+#userId")
    public List<Map> getUserMenuList(Long userId) {

        //系统管理员，拥有最高权限
        if(userId.longValue() == Constant.SUPER_ADMIN){
            return getAllMenuList(null);
        }
        //用户菜单列表
        List<Long> menuIdList = userService.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }

    /**
     * 获取所有菜单列表
     */
    private List<Map> getAllMenuList(List<Long> menuIdList){
        //查询根菜单列表
        List<Map> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }

    /**
     * 获取目录和菜单 递归
     */
    private List<Map> getMenuTreeList(List<Map> menuList, List<Long> menuIdList){
        List<Map> subMenuList = new ArrayList<Map>();
        for(Map entity : menuList){
            //目录
            if(Integer.parseInt(entity.get("type")+"") == Constant.MenuType.CATALOG.getValue()){
                entity.put("list",getMenuTreeList(queryListParentId(Long.parseLong(entity.get("id")+""), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }

    /**
     * 获取所有菜单 递归
     */
    private List<Map> getAllMenuTreeList(List<Map> menuList){
        List<Map> subMenuList = new ArrayList<Map>();

        for(Map entity : menuList){
            //目录
            if(Integer.parseInt(entity.get("type")+"") == Constant.MenuType.CATALOG.getValue()||Integer.parseInt(entity.get("type")+"") == Constant.MenuType.MENU.getValue()){
                entity.put("children",getAllMenuTreeList(queryListParentId(Long.parseLong(entity.get("id")+""))));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }
}
