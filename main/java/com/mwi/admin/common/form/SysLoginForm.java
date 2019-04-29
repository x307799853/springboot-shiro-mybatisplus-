package com.mwi.admin.common.form;

import lombok.Data;

/**
 * @Auther: Eric
 * @Description:
 */
@Data
public class SysLoginForm {
    private String username;
    private String password;
    private String captcha;
    private String uuid;
}
SysUserController