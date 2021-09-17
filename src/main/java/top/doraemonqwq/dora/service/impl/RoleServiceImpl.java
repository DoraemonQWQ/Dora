package top.doraemonqwq.dora.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.dao.mapper.RoleMapper;
import top.doraemonqwq.dora.entity.security.Role;
import top.doraemonqwq.dora.service.RoleService;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Doraemon
 * RoleService的业务实现类
 */
@Service("RoleServiceImpl")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 存储role表的全部数据
     */
    private List<Role> roleList = null;

    private final String ROLEID = "roleId";
    private final String ROLENAME = "roleName";
    private final String ROLEEXPLANATION = "roleExplanation";

    @Override
    public List<Role> selectRole() {
        roleList = roleMapper.selectAllRole();
        return roleList;
    }

    @Override
    public Role selectRole(int roleId) {
        if (roleList == null) {
            roleList = selectRole();
        }
        for (Role role : roleList) {
            if (role.getRoleId() == roleId) {
                return role;
            }
        }
        return null;
    }

    @Override
    public Role selectRole(String roleName) {
        if (roleList == null) {
            roleList = selectRole();
        }
        for (Role role : roleList) {
            if (roleName.equals(role.getRoleName())) {
                return role;
            }
        }
        return null;
    }

    @Override
    public int insertRole(Role role) {
        if (selectRole(role.getRoleId()) != null) {
            return 0;
        }
        int i = roleMapper.insertRole(role);
        if (i > 0) {
            roleList = selectRole();
        }
        return i;
    }

    @Override
    public int deleteRole(int roleId) {
        if (selectRole(roleId) == null) {
            return 0;
        }
        int i = roleMapper.deleteRole(roleId);
        if (i > 0) {
            roleList = selectRole();
        }
        return i;
    }

    @Override
    public int updateNameRole(int roleId, String roleName) {
        if (selectRole(roleId) == null || ("".equals(roleName) || roleName == null)) {
            return 0;
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put(ROLEID, roleId);
        map.put(ROLENAME, roleName);
        int i = roleMapper.updateRole(map);
        return i;
    }

    @Override
    public int updateExplanationRole(int roleId, String roleExplanation) {
        if (selectRole(roleId) == null || ("".equals(roleExplanation) || roleExplanation == null)) {
            return 0;
        }
        Map<String,Object> map = new HashMap<>(3);
        map.put(ROLEID,roleId);
        map.put(ROLEEXPLANATION,roleExplanation);
        int i = roleMapper.updateRole(map);
        return i;
    }

}
