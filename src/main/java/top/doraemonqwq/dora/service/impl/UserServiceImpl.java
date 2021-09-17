package top.doraemonqwq.dora.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.dao.mapper.UserMapper;
import top.doraemonqwq.dora.entity.pojo.User;
import top.doraemonqwq.dora.service.UserService;
import top.doraemonqwq.dora.utils.RedisUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Doraemon
 * UserService的实现类
 */
@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    private final String USERS = "users";
    private final String USERTOKEN = "_Token";
    private final String USERID = "userId";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String EMAIL = "email";
    private final String LAST_LOGIN_TIME = "lastLoginTime";
    private final String TOKEN = "token";

    @Override
    public User selectUser(Integer userId) {
        // 查询redis中有没有这个用户的数据
        if (redisUtil.hasKey(String.valueOf(userId))) {
            // 如果有，那么直接取redis中的数据
            JSON userJson = (JSON) redisUtil.get(String.valueOf(userId));
            User user = userJson.toBean(User.class);
            return user;
        }
        // 如果没有，则查询mysql中的数据，并且存放到redis中
        User user = userMapper.selectByUserIdUser(userId);
        if (user != null) {
            JSON userJson = JSONUtil.parse(user);
            redisUtil.set(String.valueOf(userId), userJson);
        }
        return user;
    }

    @Override
    public User selectUser(String username) {
        return userMapper.selectByUsernameUser(username);
    }

    @Override
    public List<User> selectUser() {
        // 先查询redis中有没有数据
        if (redisUtil.hasKey(USERS)) {
            // 有则使用
            JSON usersJson = (JSON) redisUtil.get(USERS);
            List<User> users = JSONUtil.parseArray(usersJson.toString()).toList(User.class);
            return users;
        }
        // 没有则查询mysql，然后将数据存放到redis
        List<User> users = userMapper.selectAllUser();
        JSON usersJson = JSONUtil.parse(users);
        redisUtil.set(USERS, usersJson);
        return users;
    }

    /**
     * 更新redis中的Users缓存
     */
    private void updateRedisByUsers() {
        // 删除旧缓存
        redisUtil.del(USERS);
        // 将新的缓存添加进去
        List<User> users = userMapper.selectAllUser();
        JSON usersJson = JSONUtil.parse(users);
        redisUtil.set(USERS, usersJson);
    }


    @Override
    public boolean insertUser(User user) {
        if (userMapper.insertUser(user) > 0 )  {
            updateRedisByUsers();
            JSON userJson = JSONUtil.parse(user);
            redisUtil.set(user.getUsername(), userJson);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        if (userMapper.deleteUser(userId) > 0) {
            redisUtil.del(String.valueOf(userId));
            updateRedisByUsers();
        }
        return false;
    }

    @Override
    public boolean updateLoginTime(Integer userId, String lastLoginTime) {
        String userIdStr = String.valueOf(userId);

        redisUtil.del(userIdStr);

        Map<String, Object> map = new HashMap<>();
        map.put(USERID, userId);
        map.put(LAST_LOGIN_TIME, lastLoginTime);

        if (userMapper.updateUser(map) > 0) {
            updateRedisByUsers();
            selectUser(userId);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUsername(Integer userId, String newUsername) {
        String userIdStr = String.valueOf(userId);

        if (userMapper.selectByUserIdUser(userId).getUsername().equals(newUsername)) {
            throw new RuntimeException("新用户名和旧用户名相同");
        }

        if (userMapper.selectByUsernameUser(newUsername) != null) {
            throw new RuntimeException("用户名已经存在");
        }

        redisUtil.del(userIdStr);

        Map<String, Object> map = new HashMap<>();
        map.put(USERID, userId);
        map.put(USERNAME, newUsername);
        if ( userMapper.updateUser(map) > 0 ) {
            // 更新redis中的缓存
            updateRedisByUsers();
            selectUser(userId);
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePassword(Integer userId, String password) {
        redisUtil.del(String.valueOf(userId));

        Map<String, Object> map = new HashMap<>();
        map.put(USERID, userId);
        map.put(PASSWORD, password);

        if (userMapper.updateUser(map) > 0) {
            updateRedisByUsers();
            selectUser(userId);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateEmail(Integer userId, String email) {
        redisUtil.del(String.valueOf(userId));

        Map<String, Object> map = new HashMap<>();
        map.put(USERID, userId);
        map.put(EMAIL, email);

        if (userMapper.updateUser(map) > 0) {
            updateRedisByUsers();
            selectUser(userId);
            return true;
        }
        return false;
    }

    @Override
    public String getToken(Integer userId) {
        if (redisUtil.hasKey(userId + USERTOKEN)) {
            return  (String) redisUtil.get(userId + USERTOKEN);
        }
        String token = userMapper.selectToken(userId);
        redisUtil.set(userId +USERTOKEN, token);
        return token;
    }

    @Override
    public boolean updateToken(Integer userId, String token) {
        Map<String, Object> map = new HashMap<>();
        // 查询redis中有没有该用户的token
        if (redisUtil.hasKey(userId + USERTOKEN)) {
            // 如果有，那就删除
            redisUtil.del(userId + USERTOKEN);
        }
        // 如果没有，那么取消删除部分
        map.put(USERID, userId);
        map.put(TOKEN, token);

        // 如果返回的成功数不为0，那么就将当前的token存入redis中
        if (userMapper.updateUser(map) > 0) {
            redisUtil.set(userId + USERTOKEN, token);
            return true;
        }
        return false;
    }

    @Override
    public boolean delToken(Integer userId) {

        redisUtil.del(userId + USERTOKEN);
        Map<String, Object> map = new HashMap<>();
        map.put(USERID, userId);
        map.put(TOKEN, "null");
        int i = userMapper.updateUser(map);
        return i > 0;

    }
}
