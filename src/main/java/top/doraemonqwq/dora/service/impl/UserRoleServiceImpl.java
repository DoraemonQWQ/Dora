package top.doraemonqwq.dora.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.dao.mapper.UserRoleMapper;
import top.doraemonqwq.dora.service.RoleService;
import top.doraemonqwq.dora.service.UserRoleService;
import top.doraemonqwq.dora.service.UserService;

import java.util.List;

/**
 * @author Doraemon
 * @date 2021-09-10
 * UserRoleService实现类
 */
@Service("UserRoleServiceImpl")
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @Override
    public List<String> listRoleNames(String username) {
        List<String> strings = userRoleMapper.selectRoleNameByUsername(username);
        return strings;

    }

    @Override
    public boolean insertUserRoleAss(String username, List<String> roles) {
        Integer userId = userService.selectUser(username).getUserId();

        for (String role : roles) {
            Integer roleId = roleService.selectRole(role).getRoleId();
            int i = userRoleMapper.insertUserRoleAss(userId, roleId);
            if (i == 0) {
                log.warn("添加失败，有重复的roleId");
            }
        }

        return true;
    }
}
