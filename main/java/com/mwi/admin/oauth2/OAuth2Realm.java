package com.mwi.admin.oauth2;

import com.mwi.admin.core.utils.JwtUtils;
import com.mwi.admin.modules.sysUser.entity.SysUser;
import com.mwi.admin.modules.sysUser.service.ISysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Author Eric
 * @Date 4/17/2019
 **/
@Component
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ISysUserService userService;

    /**
     * for permission
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser user = (SysUser) principals.getPrimaryPrincipal();
        Long userId = user.getUserId();
        // get permission code of user
        Set<String> permsSet = userService.getUserPermissions(userId);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * Certification for login
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String accessToken = (String) token.getPrincipal();
        //check the token if expired
        if (jwtUtils.isTokenExpired(accessToken)) {
            //TODO i18n message
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        // search user info
        String username = jwtUtils.getUsernameFromToken(accessToken);

        SysUser user = userService.findByUsername(username);

        if (user.getStatus() == 0) {
            throw new LockedAccountException("Account is locked , please contact admin manager");
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
        return info;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AuthenticationToken;
    }
}
