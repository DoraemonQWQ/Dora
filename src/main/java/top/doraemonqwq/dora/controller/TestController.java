package top.doraemonqwq.dora.controller;

import cn.hutool.json.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.doraemonqwq.dora.aop.UserLog;
import top.doraemonqwq.dora.config.NonStaticResourceHttpRequestHandler;
import top.doraemonqwq.dora.constant.ResourceUrlConstants;
import top.doraemonqwq.dora.security.pojo.ResponseResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @PostMapping("/file")
    @PreAuthorize("hasAnyRole('ROLE_USER_G','ROLE_USER_R18')")
    public JSON testFile(@RequestParam("file") MultipartFile file) {

        if (file == null) {
            return ResponseResult.create().ok(null, "/api/test/file", "上传失败");
        }


        System.out.println(file.getSize());


        return ResponseResult.create().ok(null, "/api/test/file", "上传成功");
    }

    @Autowired
    NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    @GetMapping("/video/{id}")
    public void testVideo(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String resourcePath = this.getClass().getClassLoader().getResource("").getPath().substring(1);
        // 非unix系统，前面不用加 /
//        String mp4Path = resourcePath + "static/video/" + id + ".mp4";
//        String mp4Path = "Users/hequanshawu/Desktop/JiJiDown.COM 崩坏学园2 德丽莎角色歌 星与你消失之日 PV Av5399806 P1 .mp4";
        String mp4Path = ResourceUrlConstants.VIDEO_PATH + id + ".mp4";
//        Path path = Paths.get(mp4Path);
//        if (Files.exists(path)) {
//            // 获取文件类型
//            String contentType = Files.probeContentType(path);
//            if (!StrUtil.isEmpty(contentType)) {
//                // 设置response
//                response.setContentType(contentType);
//            }
//            // 添加请求头
//            request.setAttribute(nonStaticResourceHttpRequestHandler.filepath, path);
//            // 利用 nonStaticResourceHttpRequestHandler.handleRequest() 实现返回视频流
//            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
//        } else {
//            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
//            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
//        }
    }


    @RequestMapping("/token")
    @UserLog("测试记录用户操作")
    public void testToken() {
        // 获取上下文
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            System.out.println("找不到上下文");
        } else {
            Object principal =  authentication.getPrincipal();
            if ("anonymousUser".equals(principal.toString())) {
                System.out.println("anonymousUser".equals(principal.toString()));
                System.out.println(principal);
            } else {
                Integer value = Integer.valueOf(principal.toString());
                System.out.println(value);
            }

        }
    }

    @RequestMapping("/auth")
    @PreAuthorize("hasAnyRole('ROLE_USER_G','ROLE_USER_R18')")
    public void testAuthority() {
        // 获取上下文
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            System.out.println("找不到上下文");
        } else {
            Object principal = authentication.getPrincipal();
            System.out.println(principal);
        }
    }
}
