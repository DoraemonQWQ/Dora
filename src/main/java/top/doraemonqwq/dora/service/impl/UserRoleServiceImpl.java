package top.doraemonqwq.dora.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.dao.mapper.RoleMapper;
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
                throw new RuntimeException("添加失败，有重复的roleId");
            }
        }

        return true;
    }
}
