package top.doraemonqwq.dora.controller;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSON;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.doraemonqwq.dora.security.pojo.ResponseResult;
import top.doraemonqwq.dora.service.UserService;

/**
 * @author Doraemon
 * @date 2021-09-17
 * 修改用户信息api
 */
@RestController
@RequestMapping("/api/alter/")
public class AlterController {

    @Autowired
    @Qualifier("UserServiceImpl")
    UserService userService;


    @RequestMapping("/username")
    @ResponseBody
    public JSON alterUsername(String username) {
        // 从上下文中取出当前用户的id
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean b = userService.updateUsername(userId, username);

        if (!b) {
            throw new RuntimeException("修改用户名失败，用户id：" + userId);
        }


        return ResponseResult.create().ok(null, "/api/alter/username", b ? "修改成功" : "修改失败");
    }


}
