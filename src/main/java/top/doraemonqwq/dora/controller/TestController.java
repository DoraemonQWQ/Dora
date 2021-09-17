package top.doraemonqwq.dora.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @RequestMapping("/r18")
    @PreAuthorize("hasRole('ROLE_USER_R18')")
    public String testR18() {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "成功进入R18频道，ID = " + userId;
    }

    @RequestMapping("/g")
    @PreAuthorize("hasRole('ROLE_USER_G')")
    public String testG() {
        Integer userId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "成功进入全年龄频道，ID = " + userId;
    }

}
