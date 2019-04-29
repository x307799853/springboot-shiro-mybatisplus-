package com.mwi.admin.modules.sysUser.mapper;

import com.mwi.admin.modules.sysUser.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author Eric
 * @since 2019-04-18
 */
@Component
public interface SysUserMapper extends BaseMapper<SysUser> {


    @Select("select m.perms from sys_user_role ur "
            + "LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id "
            + "LEFT JOIN sys_menu m on rm.menu_id = m.menu_id " + "where ur.user_id = #{userId}")
    List<String> queryAllPerms(Long userId);
}
