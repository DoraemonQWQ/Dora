package top.doraemonqwq.dora.controller;

import cn.hutool.json.JSON;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.doraemonqwq.dora.security.pojo.ResponseResult;

import java.util.Map;

/**
 * @author Doraemon
 * @date 2021-09-18
 * 展示数据api
 */
@RestController
@RequestMapping("/api/show-all-ages")
public class ShowAllAgesController {

    @PostMapping("/show-home")
    public JSON showHome() {

        return ResponseResult.create().ok(null, "/api/show/show-home/", "请求成功，返回全年龄分级主页数据");

    }

    @PostMapping("/show-detail")
    public JSON showDetail(@RequestBody Integer id) {
        if (id == null) {
            return ResponseResult.create().ok(null, "/api/show/show-detail/", "请求失败，请求参数为null");
        }

        // 查询数据库中对应id的动漫详情数据

        return ResponseResult.create().ok(null, "/api/show/show-detail/", ("请求成功，返回" + id + "页面"));

    }

    @PostMapping("/show-video")
    public JSON showVideo(@RequestBody Map<String, Object> map) {
        if (map == null || map.size() == 0) {
            return ResponseResult.create().ok(null, "/api/show/show-video/", "请求失败，请求参数为null");
        }

        // key值为动漫的id，value值为级数的id

        System.out.println(map);

        return ResponseResult.create().ok(null, "/api/show/show-video/", "请求成功，返回动漫级数");

    }

}
