package top.doraemonqwq.dora.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.doraemonqwq.dora.constant.RoleConstants;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = top.doraemonqwq.dora.DoraApplication.class)
public class UserRoleServiceImplTest {

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Test
    public void listRoleNamesTest() {
        List<String> admin = userRoleService.listRoleNames("admin");
        System.out.println(admin);
    }

    @Test
    public void insertUserRoleAssTest() {
        List<String> strings = new ArrayList<>();
        strings.add(RoleConstants.ROLE_USER_DEFAULT);
        boolean root = userRoleService.insertUserRoleAss("root", strings);
        System.out.println(root);
    }

}
