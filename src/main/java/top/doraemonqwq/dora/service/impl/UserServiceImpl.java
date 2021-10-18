package top.doraemonqwq.dora.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    private final String REDIS_USER_ALL_NAME = "users";
    private final String REDIS_USER_TOKEN_SUFFIX = "_Token";
    private final String REDIS_USER_ID_PREFIX = "user_";
    private final String USERID = "userId";
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String EMAIL = "email";
    private final String LAST_LOGIN_TIME = "lastLoginTime";
    private final String INTRODUCTION = "introduction";
    private final String TOKEN = "token";

    @Override
    public User selectUser(Integer userId) {
        // 查询redis中有没有这个用户的数据
        if (redisUtil.hasKey(REDIS_USER_ID_PREFIX + userId)) {
            // 如果有，那么直接取redis中的数据
            JSON userJson = (JSON) redisUtil.get(REDIS_USER_ID_PREFIX + userId);
            return userJson.toBean(User.class);
        }
        // 如果没有，则查询mysql中的数据，并且存放到redis中
        User user = userMapper.selectByUserIdUser(userId);
        if (user != null) {
            JSON userJson = JSONUtil.parse(user);
            redisUtil.set(REDIS_USER_ID_PREFIX + userId, userJson);
        }
        return user;
    }

    @Override
    public User selectUser(String username) {
        return userMapper.selectByUsernameUser(username);
    }

    @Override
    public User selectUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }

    @Override
    public List<User> selectUser() {
        // 先查询redis中有没有数据
        if (redisUtil.hasKey(REDIS_USER_ALL_NAME)) {
            // 有则使用
            JSON usersJson = (JSON) redisUtil.get(REDIS_USER_ALL_NAME);
            return JSONUtil.parseArray(usersJson.toString()).toList(User.class);
        }
        // 没有则查询mysql，然后将数据存放到redis
        List<User> users = userMapper.selectAllUser();
        JSON usersJson = JSONUtil.parse(users);
        redisUtil.set(REDIS_USER_ALL_NAME, usersJson);
        return users;
    }

    /**
     * 更新redis中的Users缓存
     */
    private void updateRedisByUsers() {
        // 删除旧缓存
        redisUtil.del(REDIS_USER_ALL_NAME);
        // 将新的缓存添加进去
        List<User> users = userMapper.selectAllUser();
        JSON usersJson = JSONUtil.parse(users);
        redisUtil.set(REDIS_USER_ALL_NAME, usersJson);
    }


    @Override
    public boolean insertUser(User user) {
        if (userMapper.insertUser(user) > 0 )  {
            updateRedisByUsers();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        if (userMapper.deleteUser(userId) > 0) {
            redisUtil.del(REDIS_USER_ID_PREFIX + userId);
            updateRedisByUsers();
            return true;
        }
        return false;
    }

    @Override
    public boolean updateLoginTime(Integer userId, String lastLoginTime) {
        if (objectIsNull(lastLoginTime)) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        redisUtil.del(REDIS_USER_ID_PREFIX + userId);

        Map<String, Object> map = new HashMap<>();
        map.put(USERID, userId);
        map.put(LAST_LOGIN_TIME, lastLoginTime);

        if (userMapper.updateUser(map) > 0) {
            updateRedisByUsers();
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUsername(Integer userId, String newUsername) {

        if (objectIsNull(newUsername)) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }


        if (userMapper.selectByUserIdUser(userId).getUsername().equals(newUsername)) {
            log.warn("新用户名和旧用户名相同");
        }

        if (userMapper.selectByUsernameUser(newUsername) != null) {
            log.warn("用户名已经存在");
        }

        redisUtil.del(REDIS_USER_ID_PREFIX + userId);

        Map<String, Object> map = new HashMap<>();
        map.put(USERID, userId);
        map.put(USERNAME, newUsername);
        if ( userMapper.updateUser(map) > 0 ) {
            // 更新redis中的缓存
            updateRedisByUsers();
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePassword(Integer userId, String password) {

        if (objectIsNull(password)) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        redisUtil.del(REDIS_USER_ID_PREFIX + userId);

        Map<String, Object> map = new HashMap<>();
        map.put(USERID, userId);
        map.put(PASSWORD, password);

        if (userMapper.updateUser(map) > 0) {
            updateRedisByUsers();
            return true;
        }
        return false;
    }

    @Override
    public boolean updateEmail(Integer userId, String email) {

        if (objectIsNull(email)) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        redisUtil.del(REDIS_USER_ID_PREFIX + userId);

        Map<String, Object> map = new HashMap<>();
        map.put(USERID, userId);
        map.put(EMAIL, email);

        if (userMapper.updateUser(map) > 0) {
            updateRedisByUsers();
            return true;
        }
        return false;
    }

    /**
     * 更新用户个人简介
     *
     * @param userId       需要更新邮箱的用户id
     * @param introduction 新的个人简介
     * @return 成功返回true，失败返回false
     */
    @Override
    public boolean updateIntroduction(Integer userId, String introduction) {
        if (objectIsNull(introduction)) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        redisUtil.del(REDIS_USER_ID_PREFIX + userId);

        Map<String, Object> map = new HashMap<>();
        map.put(USERID, userId);
        map.put(INTRODUCTION, introduction);

        if (userMapper.updateUser(map) > 0) {
            updateRedisByUsers();
            return true;
        }
        return false;
    }

    @Override
    public String getToken(Integer userId) {
        String redisUserTokenName = REDIS_USER_ID_PREFIX + userId + REDIS_USER_TOKEN_SUFFIX;

        if (redisUtil.hasKey(redisUserTokenName)) {
            return  (String) redisUtil.get(redisUserTokenName);
        }
        String token = userMapper.selectToken(userId);
        redisUtil.set(redisUserTokenName, token);
        return token;
    }

    @Override
    public boolean updateToken(Integer userId, String token) {
        String redisUserTokenName = REDIS_USER_ID_PREFIX + userId + REDIS_USER_TOKEN_SUFFIX;
        Map<String, Object> map = new HashMap<>();
        // 查询redis中有没有该用户的token
        if (redisUtil.hasKey(redisUserTokenName)) {
            // 如果有，那就删除
            redisUtil.del(redisUserTokenName);
        }
        // 如果没有，那么取消删除部分
        map.put(USERID, userId);
        map.put(TOKEN, token);

        // 如果返回的成功数不为0，那么就将当前的token存入redis中
        if (userMapper.updateUser(map) > 0) {
            redisUtil.set(redisUserTokenName, token);
            return true;
        }
        return false;
    }

    @Override
    public boolean delToken(Integer userId) {
        String redisUserTokenName = REDIS_USER_ID_PREFIX + userId + REDIS_USER_TOKEN_SUFFIX;

        redisUtil.del(redisUserTokenName);
        Map<String, Object> map = new HashMap<>();
        map.put(USERID, userId);
        map.put(TOKEN, "null");
        int i = userMapper.updateUser(map);
        return i > 0;

    }

    /**
     * 判断数据是否为空
     * @return 为空返回true，不为空返回false
     */
    private boolean objectIsNull(Object obj) {
        return obj == null;
    }

}
