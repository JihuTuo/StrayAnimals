package com.hsshy.beam.admin.modular.sys.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hsshy.beam.admin.modular.sys.entity.Dept;

import java.util.List;

/**
 * 部门管理
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-10-10 21:13:03
 */
public interface IDeptService extends IService<Dept> {


    /**
     * 获取树形列表
     */
    List<Dept> treeDeptList(Long deptId, Dept dept);

    List<Dept> queryListParentId(Long parentId);




}
