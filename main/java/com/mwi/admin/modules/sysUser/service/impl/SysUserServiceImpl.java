package com.mwi.admin.modules.sysUser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mwi.admin.core.Constant;
import com.mwi.admin.modules.sysMenu.entity.SysMenu;
import com.mwi.admin.modules.sysMenu.mapper.SysMenuMapper;
import com.mwi.admin.modules.sysUser.entity.SysUser;
import com.mwi.admin.modules.sysUser.mapper.SysUserMapper;
import com.mwi.admin.modules.sysUser.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author Eric
 * @since 2019-04-18
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysUserMapper userMapper;


    @Override
    public SysUser findByUsername(String username) {
        QueryWrapper mapper = new QueryWrapper();
        mapper.eq("username", username);
        return this.getOne(mapper);
    }

    @Override
    public Set<String> getUserPermissions(Long userId) {
        List<String> permsList;
        //ADMIN account have all permissions
        if (userId == Constant.SUPER_ADMIN) {
            List<SysMenu> menuList = menuMapper.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (SysMenu menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = userMapper.queryAllPerms(userId);
        }
        // filter permsList
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }
}
