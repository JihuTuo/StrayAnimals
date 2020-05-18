package com.hsshy.beam.admin.modular.sys.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hsshy.beam.admin.modular.sys.dao.DeptMapper;
import com.hsshy.beam.admin.modular.sys.entity.Dept;
import com.hsshy.beam.admin.modular.sys.service.IDeptService;
import com.hsshy.beam.common.utils.ToolUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门管理
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-10-10 21:13:03
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {


    @Override
    public List<Dept> treeDeptList(Long deptId, Dept dept) {
        List<Dept> deptList ;
        if(ToolUtil.isNotEmpty(dept.getName())){
            deptList = this.list(new QueryWrapper<Dept>().lambda().like(Dept::getName,dept.getName()));
        }
        else {
            deptList =  queryListParentId(deptId);
        }

        return getAllDeptTreeList(deptList);
    }

    @Override
    public List<Dept> queryListParentId(Long parentId) {
        return baseMapper.queryListParentId(parentId);
    }




    /**
     * 部门树形表格
     */
    private List<Dept> getAllDeptTreeList(List<Dept> deptList){
        List<Dept> subDeptList = new ArrayList<Dept>();

        for(Dept entity : deptList){
            entity.setChildren(getAllDeptTreeList(queryListParentId(entity.getId())));
            subDeptList.add(entity);
        }

        return subDeptList;
    }





}
