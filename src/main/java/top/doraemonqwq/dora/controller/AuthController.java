package top.doraemonqwq.dora.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.doraemonqwq.dora.constant.SecurityConstants;
import top.doraemonqwq.dora.dto.UserLoggingDTO;
import top.doraemonqwq.dora.dto.UserLoginDTO;
import top.doraemonqwq.dora.security.pojo.JwtUser;
import top.doraemonqwq.dora.security.pojo.ResponseResult;
import top.doraemonqwq.dora.service.AuthService;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Doraemon
 * @date 2021-09-10
 * 用户登录授权api
 */
@RestController
@RequestMapping("/api/auth")
@Api("用户登录注册Controller")
public class AuthController {

    @Autowired
    @Qualifier("AuthServiceImpl")
    AuthService authService;

    @PostMapping("/login")
    @ApiOperation("登录api -- /api/auth/login")
    @ApiParam("登录的用户dto对象 -- UserLoginDTO")
    public JSON authLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
        // 验证用户信息 得到一个封装好的JwtUser
        JwtUser jwtUser = authService.authLogin(userLoginDTO);

        if (jwtUser.isNull()) {
            return ResponseResult.create().ok(JwtUser.create().toJSON(), "/api/auth/login", jwtUser.getError());
        }

        // 将JwtUser中的token的开头添加标识
        jwtUser.addTokenBearer(SecurityConstants.TOKEN_PREFIX);

        // 添加响应头，设置请求头名称 添加token到指定请求头中
        response.addHeader("Access-Control-Expose-Headers",SecurityConstants.TOKEN_HEADER);
        response.addHeader(SecurityConstants.TOKEN_HEADER, jwtUser.getToken());

        // 将JwtUser转为JSON返回给前端
        return ResponseResult.create().ok(JSONUtil.parse(jwtUser.getUserDTO()), "/api/auth/login", "登录成功");
    }

    @PostMapping("/logging")
    @ApiOperation("登录api -- /api/auth/logging")
    @ApiParam("注册的用户dto对象 -- UserLogginDTO")
    public JSON authLogging(@RequestBody UserLoggingDTO userLoggingDTO, HttpServletResponse response) {
        // 注册用户，得到一个封装到的JwtUser
        JwtUser jwtUser = authService.authLogging(userLoggingDTO);

        // 将JwtUser中的token添加开头标识
        jwtUser.addTokenBearer(SecurityConstants.TOKEN_PREFIX);

        // 添加响应头，设置请求头名称 添加token到指定请求头中
        response.addHeader("Access-Control-Expose-Headers",SecurityConstants.TOKEN_HEADER);
        response.addHeader(SecurityConstants.TOKEN_HEADER, jwtUser.getToken());

        // 将jwtUser转换为JSON返回给前端
        return ResponseResult.create().ok(JSONUtil.parse(jwtUser.getUserDTO()), "/api/auth/logging", "注册成功");

    }

    @PostMapping("/logout")
    @ApiOperation("注销登录api -- 删除用户在服务器中缓存的token")
    public void authLogout() {
        // 从security上下文中获取id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = (Integer) authentication.getPrincipal();
        authService.authLogout(userId);
    }

}
