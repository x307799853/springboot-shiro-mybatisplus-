package com.mwi.admin.modules.sysUser.controller;


import com.mwi.admin.common.form.SysLoginForm;
import com.mwi.admin.common.utils.R;
import com.mwi.admin.core.utils.JwtUtils;
import com.mwi.admin.modules.sysUser.entity.SysUser;
import com.mwi.admin.modules.sysUser.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author Eric
 * @since 2019-04-18
 */
@RestController
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Login
     */
    @PostMapping("/sys/login")
    public Map<String, Object> login(@RequestBody SysLoginForm form) throws IOException {
        //TODO  captcha
        //user infomation
        SysUser user = userService.findByUsername(form.getUsername());
        // account not exist
        if (user == null) {
            return R.error("账号不存在");
        }
        if (!user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
            return R.error("账号或密码不正确");
        }
        //account has been locked
        if (user.getStatus() == 0) {
            return R.error("账号已被锁定,请联系管理员");
        }
        String token = jwtUtils.generateToken(user.getUsername());
        long expire = jwtUtils.getExpire() / 1000;
        R r = R.ok().put("token", token).put("expire", expire);
        return r;
    }

    @RequestMapping("test")
    @RequiresPermissions("sys:schedule:save")
    public Object test() {

        return R.ok();
    }


}
