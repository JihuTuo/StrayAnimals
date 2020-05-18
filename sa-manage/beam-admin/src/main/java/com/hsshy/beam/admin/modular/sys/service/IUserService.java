package com.hsshy.beam.admin.modular.sys.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hsshy.beam.admin.modular.sys.dto.ChangePassowdForm;
import com.hsshy.beam.admin.modular.sys.entity.User;
import com.hsshy.beam.web.modular.base.service.ICommonService;
import com.hsshy.beam.common.utils.R;
import java.util.List;
import java.util.Map;

/**
 * 管理员表
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2018-10-07 18:03:20
 */
public interface IUserService extends ICommonService<User> {

    IPage<Map> selectPageList(User user);

    R saveUser(User user);

    R changeStatus(Long userId, Integer flag);

    R deleteUser(Long userIds[]);

    R resetPassword(Long userIds[]);

    R changePassword(ChangePassowdForm changePassowdForm);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

//    List<String> queryAllButtonPerms(Long userId,Integer type);


}
