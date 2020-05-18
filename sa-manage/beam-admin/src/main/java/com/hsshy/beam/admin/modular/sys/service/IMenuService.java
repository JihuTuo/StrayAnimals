package com.hsshy.beam.admin.modular.sys.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hsshy.beam.admin.modular.sys.entity.Menu;
import com.hsshy.beam.common.utils.R;
import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-10-08 16:33:17
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @param menuIdList  用户菜单ID
     */
    List<Map> queryListParentId(Long parentId, List<Long> menuIdList);

    /**
     * 获取该用户的菜单列表
     */
    List<Map> getUserMenuList(Long userId);

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    List<Map> queryListParentId(Long parentId);

    /**
     * 获取树形菜单列表
     */
    List<Map> treeMenuList(Long menuId, Menu menu);

    R deleteMenu(Long menuIds[]);



}
