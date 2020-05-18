package com.hsshy.beam.common.constant;

/**
 * 缓存标识前缀集合,常用在ConstantFactory类中
 *
 * @author fengshuonan
 * @date 2017-04-25 9:37
 */
public interface CacheKey {

    /**
     * 常量缓存
     */
    String CONSTANT = "CONSTANT";
    /**
     * 角色名称(多个)
     */
    String ROLES_NAME = "roles_name_";
    /**
     * 角色名称(单个)
     */
    String SINGLE_ROLE_NAME = "single_role_name_";
    /**
     * 部门名称
     */
    String DEPT_NAME = "dept_name_";

    String USER_ID = "USER_ID_";

    String USER_MENU = "USER_MENU_";

    String USER_BUTTON = "USER_BUTTON_";

}
