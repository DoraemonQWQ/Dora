package top.doraemonqwq.dora.service.impl;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.doraemonqwq.dora.constant.RoleConstants;
import top.doraemonqwq.dora.dto.UserDTO;
import top.doraemonqwq.dora.dto.UserLoggingDTO;
import top.doraemonqwq.dora.dto.UserLoginDTO;
import top.doraemonqwq.dora.entity.pojo.ImagePojo;
import top.doraemonqwq.dora.entity.pojo.User;
import top.doraemonqwq.dora.security.pojo.JwtUser;
import top.doraemonqwq.dora.service.AuthService;
import top.doraemonqwq.dora.service.ImagesService;
import top.doraemonqwq.dora.service.UserRoleService;
import top.doraemonqwq.dora.service.UserService;
import top.doraemonqwq.dora.utils.HttpContextUtil;
import top.doraemonqwq.dora.utils.IpUtil;
import top.doraemonqwq.dora.utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Doraemon
 * @date 2021-09-10
 * 用户授权业务实现类
 */
@Service("AuthServiceImpl")
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    @Qualifier("UserServiceImpl")
    UserService userService;
    @Autowired
    @Qualifier("UserRoleServiceImpl")
    UserRoleService userRoleService;
    @Autowired
    @Qualifier("ImagesServiceImpl")
    ImagesService imagesService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public JwtUser authLogin(UserLoginDTO userLoginDTO) {
        String method = userLoginDTO.getMethod();
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        boolean isRemember = userLoginDTO.getRememberMe();
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        User user = null;

        // 判断用户的登录方式
        if (method.equals(UserLoginDTO.METHOD_USERNAME)) {
            // 查询用户是否存在
            user = userService.selectUser(username);
        } else if (method.equals(UserLoginDTO.METHOD_EMAIL)) {
            user = userService.selectUserByEmail(username);
        }

        // 如果不存在，则返回带错误信息的JwtUser
        if (user == null) {
            // 判断登录方式，返回错误信息
            if (method.equals(UserLoginDTO.METHOD_USERNAME)){
                log.info(IpUtil.getIpAddr(request) + "-登录错误:用户名不存在");
                return JwtUser.create().setError("用户名不存在");
            } else if (method.equals(UserLoginDTO.METHOD_EMAIL)) {
                log.info(IpUtil.getIpAddr(request) + "-登录错误:邮箱不存在");
                return JwtUser.create().setError("邮箱不存在");
            }
        }
        // 判断用户的密码是否正确，不正确返回带错误信息的JwtUser
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            log.info(username + ":验证密码错误");
            return JwtUser.create().setError("密码错误");
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

        // 通过id查找用户所拥有的图片
        List<ImagePojo> imagePojos = imagesService.selectImagesAssByUserId(user.getUserId());

        // 将用户信息保存到UserDTO对象中
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setRoles(roles);
        userDTO.setEmail(user.getEmail());
        userDTO.setLastLoginTime(user.getLastLoginTime());
        userDTO.setImages(imagePojos);

        // 将token和UserDTO封装成JwtUser后返回
        return JwtUser.create().setToken(token).setUserDTO(userDTO);
    }

    @Override
    @Transactional
    public JwtUser authLogging(UserLoggingDTO userLoggingDTO) {
        String username = userLoggingDTO.getUsername();
        String password = userLoggingDTO.getPassword();
        String email = userLoggingDTO.getEmail();
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();

        String passwordBCrypt = bCryptPasswordEncoder.encode(password.trim());

        // 获得注册时间，也是第一次登录的时间
        String date = DateUtil.date().toDateStr();
        User newUser = new User(username, passwordBCrypt, email, date, date);

        // 判断用户名和邮箱是否存在
        if (userService.selectUser(username) != null) {
            log.info(IpUtil.getIpAddr(request) + "-注册错误:用户名存在");
            return JwtUser.create().setError("用户名存在");
        } else if (userService.selectUserByEmail(email) != null) {
            log.info(IpUtil.getIpAddr(request) + "-注册错误:邮箱存在");
            return JwtUser.create().setError("邮箱存在");
        } else {
            // 如果都不存在，那么就执行写入数据库
            userService.insertUser(newUser);
        }

//        // 将新的User添加到表中，并接收添加boolean值，如果为false，那么就打印注册失败
//        if (!userService.insertUser(newUser)) {
//            throw new RuntimeException("注册失败！用户名重复！");
//        }
        // 给新用户添加普通用户角色，并添加到数据库中
        List<String> roles = new ArrayList<>();
        roles.add(RoleConstants.ROLE_USER_DEFAULT);

        if (!userRoleService.insertUserRoleAss(username, roles)) {
            log.info( IpUtil.getIpAddr(request)+ "-注册失败！添加相同的角色！");
        }

        // 查询新用户的id
        Integer userId = userService.selectUser(username).getUserId();
        // 生成token
        String token = JwtUtil.generateToken(userId, roles, true);

        // 将token放入数据库
        userService.updateToken(userId, token);

        // 给新用户添加默认头像和个人主页的默认背景
        int defaultAvatarId = imagesService.selectImageByName("default_avatar");
        int defaultBackgroundId = imagesService.selectImageByName("default_background");
        imagesService.addImageUserAss(userId, defaultAvatarId);
        imagesService.addImageUserAss(userId, defaultBackgroundId);


        // 查找用户拥有的图片
        List<ImagePojo> imagePojos = imagesService.selectImagesAssByUserId(userId);

        UserDTO userDTO = new UserDTO(newUser.getUsername(), newUser.getEmail(), roles, imagePojos, newUser.getLastLoginTime());

        // 将token放入security上下文中
        Authentication authentication = JwtUtil.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 返回JwtUser
        return JwtUser
                .create()
                .setUserDTO(userDTO)
                .setToken(token);

    }

    @Override
    public void authLogout(Integer userId) {
        /*
          清除redis和mysql中的token
         */
        userService.delToken(userId);

        /*
          清除security中存储的上下文
         */
        SecurityContextHolder.clearContext();

    }


}
