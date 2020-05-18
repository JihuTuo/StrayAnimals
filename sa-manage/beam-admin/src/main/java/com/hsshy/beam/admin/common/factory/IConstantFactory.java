package com.hsshy.beam.admin.common.factory;
import com.hsshy.beam.admin.modular.sys.entity.Dict;

import java.util.List;

public interface IConstantFactory {

    //根据用户ID获取其所有角色ID
    List<Long> getRoleIdsById(Long userId);

    //通过角色id获取角色名称
    String getSingleRoleName(Long roleId);

    //获取部门名称
    String getDeptName(Long deptId);

    //根据父级字典code和获取字典名称
    String getDictsByCode(String pcode, String code);

    //根据父级字典code和获取字典名称
    List<Dict> getDictListByCode(String pcode);


}
