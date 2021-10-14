package top.doraemonqwq.dora.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import top.doraemonqwq.dora.constant.SecurityConstants;
import top.doraemonqwq.dora.dto.UserLoggingDTO;
import top.doraemonqwq.dora.dto.UserLoginDTO;
import top.doraemonqwq.dora.entity.pojo.User;
import top.doraemonqwq.dora.security.pojo.JwtUser;
import top.doraemonqwq.dora.security.pojo.ResponseResult;
import top.doraemonqwq.dora.service.AuthService;
import top.doraemonqwq.dora.service.impl.AuthServiceImpl;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Doraemon
 * @date 2021-09-10
 * 用户登录授权api
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    @Qualifier("AuthServiceImpl")
    AuthService authService;

    @PostMapping("/login")
    public JSON authLogin(@RequestBody UserLoginDTO userLoginDTO) {
        // 验证用户信息 得到一个封装好的JwtUser
        JwtUser jwtUser = authService.authLogin(userLoginDTO);

        if (jwtUser.isNull()) {
            return ResponseResult.create().ok(JwtUser.create().toJSON(), "/api/auth/login", "用户名或密码错误");
        }

        // 将JwtUser中的token的开头添加标识
        jwtUser.addTokenBearer(SecurityConstants.TOKEN_PREFIX);

        // 添加请求头，以便之后的前端使用
        jwtUser.setHeader(SecurityConstants.TOKEN_HEADER);

//        ResponseResult.create().ok(response, jwtUser.toString(), "/api/auth/login");

        // 将JwtUser转为JSON返回给前端
        return ResponseResult.create().ok(jwtUser.toJSON(), "/api/auth/login", "登录成功");
    }

    @PostMapping("/logging")
    public JSON authLogging(@RequestBody UserLoggingDTO userLoggingDTO) {
        // 注册用户，得到一个封装到的JwtUser
        JwtUser jwtUser = authService.authLogging(userLoggingDTO);

        // 将JwtUser中的token添加开头标识
        jwtUser.addTokenBearer(SecurityConstants.TOKEN_PREFIX);

        // 添加http请求头
        jwtUser.setHeader(SecurityConstants.TOKEN_HEADER);

        // 将jwtUser转换为JSON返回给前端
        return ResponseResult.create().ok(jwtUser.toJSON(), "/api/auth/logging", "注册成功");

    }

    @PostMapping("/logout")
    public void authLogout() {
        // 从security上下文中获取id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = (Integer) authentication.getPrincipal();
        authService.authLogout(userId);
    }

}
