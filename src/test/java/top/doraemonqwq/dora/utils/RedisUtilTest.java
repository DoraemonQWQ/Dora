package top.doraemonqwq.dora.utils;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.doraemonqwq.dora.entity.pojo.User;

import java.util.Set;

@SpringBootTest
public class RedisUtilTest {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void setStringTest() {
        User user = new User("admin1", "123", "123@qq.com", "2021", "2021");
        JSON json = JSONUtil.parse(user);
        boolean set = redisUtil.set("admin1", json);
    }

    @Test
    public void hasKeyTest() {
        boolean testKey = redisUtil.hasKey("testKey");
        System.out.println(testKey ? "testKey存在" : "testKey不存在");
    }

    @Test
    public void getKeyTest() {
        String testKey = (String) redisUtil.get("testKey");
        System.out.println(testKey);
    }

    @Test
    public void deleteKeyTest() {
        redisUtil.del("10*");
    }

    @Test
    public void selectKeysByPrexTest() {
        Set<String> strings = redisUtil.scan("10*");
        System.out.println(strings);
    }

}
