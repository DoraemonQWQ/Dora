package top.doraemonqwq.dora.service.impl;

import cn.hutool.core.date.DateUtil;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Transient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.doraemonqwq.dora.constant.RoleConstants;
import top.doraemonqwq.dora.constant.SecurityConstants;
import top.doraemonqwq.dora.dto.UserDTO;
import top.doraemonqwq.dora.dto.UserLoggingDTO;
import top.doraemonqwq.dora.entity.pojo.User;
import top.doraemonqwq.dora.security.pojo.JwtUser;
import top.doraemonqwq.dora.service.AuthService;
import top.doraemonqwq.dora.service.UserRoleService;
import top.doraemonqwq.dora.service.UserService;
import top.doraemonqwq.dora.utils.JwtUtil;
import top.doraemonqwq.dora.dto.UserLoginDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Doraemon
 * @date 2021-09-10
 * 用户授权业务实现类
 */
@Service("AuthServiceImpl")
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public JwtUser authLogin(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        boolean isRemember = userLoginDTO.getRememberMe();

        // 查询用户是否存在
        User user = userService.selectUser(username);
        // 如果不存在，则直接返回空JwtUser
        if (user == null) {
            return JwtUser.create();
        }
        // 判断用户的密码是否正确，不正确返回空JwtUser
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return JwtUser.create();
        }
        // 如果密码正确，查询表获取用户的角色
        List<String> roles = userRoleService.listRoleNames(username);
        // 如果拥有的角色为空，那么就添加默认角色
        if (CollectionUtils.isEmpty(roles)) {
            roles = Collections.singletonList(RoleConstants.ROLE_USER_DEFAULT);
        }
        // 生成token
        String token = JwtUtil.generateToken(user.getUserId(), roles, isRemember);

        System.out.println(token);

        // 将token放入数据库
        userService.updateToken(user.getUserId(), token);

        // 认证成功后，将token保存到security上下文中
        Authentication authentication = JwtUtil.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 将用户信息保存到UserDTO对象中
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setRoles(roles);
        userDTO.setEmail(user.getEmail());
        userDTO.setLastLoginTime(user.getLastLoginTime());

        // 将token和UserDTO封装成JwtUser后返回
        return JwtUser.create().setToken(token).setUserDTO(userDTO);
    }

    @Override
    @Transactional
    public JwtUser authLogging(UserLoggingDTO userLoggingDTO) {
        String username = userLoggingDTO.getUsername();
        String password = userLoggingDTO.getPassword();
        String email = userLoggingDTO.getEmail();

        String passwordBCrypt = bCryptPasswordEncoder.encode(password.trim());

        // 获得注册时间，也是第一次登录的时间
        String date = DateUtil.date().toDateStr();
        User newUser = new User(username, passwordBCrypt, email, date, date);

        // 将新的User添加到表中，并接收添加boolean值，如果为false，那么就打印注册失败
        if (!userService.insertUser(newUser)) {
            throw new RuntimeException("注册失败！用户名重复！");
        }
        // 给新用户添加普通用户角色，并添加到数据库中
        List<String> roles = new ArrayList<>();
        roles.add(RoleConstants.ROLE_USER_DEFAULT);

        if (!userRoleService.insertUserRoleAss(username, roles)) {
            throw new RuntimeException("注册失败！添加相同的角色！");
        }

        // 查询新用户的id
        Integer userId = userService.selectUser(username).getUserId();
        // 生成token
        String token = JwtUtil.generateToken(userId, roles, true);

        // 将token放入数据库
        userService.updateToken(userId, token);

        // 将token放入security上下文中
        Authentication authentication = JwtUtil.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 返回JwtUser
        return JwtUser
                .create()
                .userToUserDTO(newUser, roles)
                .setToken(token);

    }

    @Override
    public void authLogout(Integer userId) {
        /**
         * 清除redis和mysql中的token
         */
        userService.delToken(userId);

        /**
         * 清除security中存储的上下文
         */
        SecurityContextHolder.clearContext();

    }


}
