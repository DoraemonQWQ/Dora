package top.doraemonqwq.dora.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.doraemonqwq.dora.DoraApplication;
import top.doraemonqwq.dora.entity.pojo.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = DoraApplication.class)
public class UserServiceImplTest {


    @Autowired
    UserServiceImpl userService;

    @Test
    public void insertUserTest() {
        User admin = new User(
                "admin",
                "123456",
                "3028029456@qq.com",
                "2021-09-06",
                "2021-09-06");
        System.out.println(userService.insertUser(admin)  ? "添加失败" : "添加成功" );
    }

    @Test
    public void selectOneUserTest() {
        User user = userService.selectUser("admina");
        System.out.println(user);
    }

    @Test
    public void selectAllUserTest() {
        List<User> users = userService.selectUser();
        for (User user : users) {
            System.out.println("selectAllUser --> user:\n" + user);
        }
    }

    @Test
    public void updateUserEmailTest() {


        userService.updateEmail(1001, "lhq3028029456@qq.com");

    }

    @Test
    public void updateUsernameTest() {
        userService.updateUsername(1002, "admin");
    }

    @Test
    public void updateLoginTimeTest() {
        userService.updateLoginTime(1002, "2021-09-07");
    }

    @Test
    public void getTokenTest() {
        String token = userService.getToken(1006);
        System.out.println(token);
    }


}
