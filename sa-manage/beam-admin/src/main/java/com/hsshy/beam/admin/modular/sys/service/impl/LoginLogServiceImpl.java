package com.hsshy.beam.admin.modular.sys.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hsshy.beam.admin.modular.sys.dao.LoginLogMapper;
import com.hsshy.beam.admin.modular.sys.entity.LoginLog;
import com.hsshy.beam.admin.modular.sys.service.ILoginLogService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 登陆日志
 *
 * @author hs
 * @email 457030599@qq.com
 * @date 2019-04-12 14:08:56
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {


    @Override
    public IPage<LoginLog> selectPageList(Page page, LoginLog loginLog) {
        return baseMapper.selectPageList(page,loginLog);
    }

    @Override
    public void deleteAll() {
        baseMapper.deleteAll();
    }
}
