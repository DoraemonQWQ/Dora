package top.doraemonqwq.dora.controller;

import cn.hutool.json.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import top.doraemonqwq.dora.aop.UserLog;
import top.doraemonqwq.dora.security.pojo.ResponseResult;
import top.doraemonqwq.dora.service.UserService;

/**
 * @author Doraemon
 * @date 2021-09-17
 * 修改用户信息api
 */
@RestController
@RequestMapping("/api/alter")
@Slf4j
@Api("修改用户数据")
public class AlterController {

    @Autowired
    @Qualifier("UserServiceImpl")
    UserService userService;


    @PostMapping("/basic-data")
    @PreAuthorize("hasAnyRole('ROLE_USER_G','ROLE_USER_R18')")
    @ApiOperation("修改用户的用户名")
    @ApiParam("新的用户名")
    @UserLog("用户名修改")
    public JSON alterUsername(@RequestBody String username) {
        // 从上下文中取出当前用户的id
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean b = userService.updateUsername(userId, username);

        if (!b) {
            log.error("修改用户名失败，用户id：" + userId);
        }

        return ResponseResult.create().ok(null, "/api/alter/username", b ? "true" : "false");
    }

    @PostMapping("/password-data")
    @PreAuthorize("hasAnyRole('ROLE_USER_G','ROLE_USER_R18')")
    @ApiOperation("修改用户的密码")
    @ApiParam("新的密码")
    @UserLog("密码修改")
    public JSON alterPassword(@RequestBody String password) {
        // 从上下文中获取当前用户的id
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean b = userService.updatePassword(userId, password);

        if (!b) {
            log.error("修改密码失败，用户id：" + userId);
        }
        return ResponseResult.create().ok(null, "/api/alter/username", b ? "true" : "false");
    }

    @PostMapping("/introduction-data")
    public JSON alterIntroduction(@RequestBody String introduction) {
        // 从上下文中获取id
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean b = userService.updateIntroduction(userId,introduction);

        if (!b) {
            log.error("修改个人简介失败，用户id：" + userId);
        }

        return ResponseResult.create().ok(null, "/api/alter/username", b ? "true" : "false");
    }


}
