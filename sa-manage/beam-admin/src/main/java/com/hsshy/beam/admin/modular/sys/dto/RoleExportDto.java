package com.hsshy.beam.admin.modular.sys.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 角色
 *
 * @author hs
 */
@Data
@NoArgsConstructor
public class RoleExportDto extends BaseRowModel {


    // 角色名称
    @ExcelProperty(value = "角色名称",index = 0)
    private String roleName;
    // 备注
    @ExcelProperty(value = "备注",index = 1)
    private String remark;




}
