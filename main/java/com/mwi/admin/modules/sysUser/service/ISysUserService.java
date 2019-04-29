package com.mwi.admin.modules.sysUser.service;

import com.mwi.admin.modules.sysUser.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author Eric
 * @since 2019-04-18
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * find user object by username
     *
     * @param username
     * @return
     */
    SysUser findByUsername(String username);

    /**
     * get permissions code
     *
     * @param userId
     * @return
     */
    Set<String> getUserPermissions(Long userId);
}
