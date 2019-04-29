package com.mwi.admin.oauth2;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Author Erick
 * @Date 4/17/2019
 **/
public class OAuth2Token implements AuthenticationToken {

    private String token;

    public OAuth2Token(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
