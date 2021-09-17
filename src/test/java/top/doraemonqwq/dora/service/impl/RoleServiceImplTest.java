package top.doraemonqwq.dora.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.doraemonqwq.dora.entity.security.Role;

import java.util.List;

@SpringBootTest(classes = top.doraemonqwq.dora.DoraApplication.class)
public class RoleServiceImplTest {

    @Autowired
    private RoleServiceImpl service;

    @Test // 查询全部 -- 成功
    public void selectAllRoleTest() {
        List<Role> roles = service.selectRole();
        System.out.println(roles);
    }

    @Test // 查询单个预期，以id查询 -- 成功
    public void selectByIdRoleTest() {
        Role role = service.selectRole(1);
        System.out.println(role);
    }

    @Test // 查询单个预期，以角色名查询 -- 成功
    public void selectByNameRoleTest() {
        Role test = service.selectRole("test");
        System.out.println(test);
    }

    @Test // 预期：(添加一个不存在的id -- 成功；添加一个已经存在的id -- 失败) -- 成功
    public void insertRoleTest() {
        Role role = new Role(1, "test", "测试专用角色");
        int i = service.insertRole(role);
        System.out.println(i > 0 ? "成功" : "失败");
    }

    @Test // 预期：(删除存在的id -- 成功；删除不存在的id -- 失败) -- 成功
    public void deleteRoleTest() {
        int i = service.deleteRole(1);
        System.out.println(i > 0 ? "成功" : "失败");
    }

    @Test // 预期：(null -- 失败；"" -- 失败；"test测试改名" -- 成功) -- 成功
    public void updateNameRoleTest() {
        int testName = service.updateNameRole(1, null);
        System.out.println(testName > 0 ? "成功" : "失败");
    }

    @Test // 预期：(null -- 失败；"" -- 失败；"测试更改角色说明" -- 成功) -- 成功
    public void updateExplanationRoleTest() {
        int i = service.updateExplanationRole(1, null);
        System.out.println(i > 0 ? "成功" : "失败");
    }


}
