package top.doraemonqwq.dora.controller;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSON;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.doraemonqwq.dora.dto.UserAlterDTO;
import top.doraemonqwq.dora.security.pojo.ResponseResult;
import top.doraemonqwq.dora.service.UserService;

/**
 * @author Doraemon
 * @date 2021-09-17
 * 修改用户信息api
 */
@RestController
@RequestMapping("/api/alter")
public class AlterController {

    @Autowired
    @Qualifier("UserServiceImpl")
    UserService userService;


    @RequestMapping("/basic-data")
    @PreAuthorize("hasAnyRole('ROLE_USER_G','ROLE_USER_R18')")
    public JSON alterUsername(@RequestBody UserAlterDTO userAlterDTO) {
        // 从上下文中取出当前用户的id
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(userAlterDTO.getUsername());

        boolean b = userService.updateUsername(userId, userAlterDTO.getUsername());

        if (!b) {
            throw new RuntimeException("修改用户名失败，用户id：" + userId);
        }


        return ResponseResult.create().ok(null, "/api/alter/username", b ? "修改成功" : "修改失败");
    }

    @RequestMapping("/password-data")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_USER_G','ROLE_USER_R18')")
    public JSON alterPassword(@RequestBody String password) {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(password);

        boolean b = userService.updatePassword(userId, password);

        if (!b) {
            throw new RuntimeException("修改密码失败，用户id：" + userId);
        }
        return ResponseResult.create().ok(null, "/api/alter/username", b ? "修改成功" : "修改失败");
    }


}
