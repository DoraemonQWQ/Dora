package top.doraemonqwq.dora.controller;

import cn.hutool.json.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.doraemonqwq.dora.aop.UserLog;
import top.doraemonqwq.dora.security.pojo.ResponseResult;
import top.doraemonqwq.dora.service.PageService;

/**
 * @author Doraemon
 * @date 2021-09-18
 * 展示数据api
 */
@RestController
@RequestMapping("/api/public-data")
@Api("公开数据请求api")
@Slf4j
public class PublicDataController {

    @Autowired
    @Qualifier("PageServiceImpl")
    PageService pageService;


    @PostMapping("/public")
    @ApiOperation("公开资源请求api")
    @ApiParam("请求的类型，有三种，anime/mmd/game -- type")
    public JSON publicData(@RequestBody String type) {
        if (type == null) {
            log.warn("请求的参数为空");
            return ResponseResult.create().ok(null, "/api/public-data/public/", "请求失败，请求的参数为空");
        }

        JSON json = pageService.publicPage(type);


        return ResponseResult.create().ok(json, "/api/public-data/public/", "请求成功，返回数据");
    }

    @PostMapping("/anime-data/years")
    @ApiOperation("动漫资源年份筛选")
    @ApiParam("年份")
    public JSON yearSelectionPageData(String years) {
        if (years == null) {
            log.warn("请求参数为空");
            return ResponseResult.create().ok(null, "/api/public-data/anime-data/years", "请求失败，请求参数为空");
        }

        JSON json = pageService.animeYearsData(years);

        return ResponseResult.create().ok(json, "/api/public-data/anime-data/years", "请求成功，返回数据");
    }

    @PostMapping("/anime-detail")
    @ApiOperation("动漫的详细页面请求api")
    @ApiParam("请求的动漫id -- id")
    @UserLog("访问动漫页面")
    public JSON animeDetailData(@RequestBody Integer id) {
        if (id == null) {
            return ResponseResult.create().ok(null, "/api/public-data/anime-detail/", "请求失败，请求参数为null");
        }

        // 查询数据库中对应id的动漫详情数据
        JSON json = pageService.animeDetailPage(id);

        return ResponseResult.create().ok(json, "/api/public-data/anime-detail/", ("请求成功，返回的动漫" + id + "页面"));

    }

    @PostMapping("/mmd-detail")
    @ApiOperation("mmd的详细页面请求api")
    @ApiParam("请求的mmdId -- id")
    @UserLog("访问mmd页面")
    public JSON mmdDetailData(@RequestBody Integer id) {
        if (id == null) {
            return ResponseResult.create().ok(null, "/api/public-data/mmd-detail/", "请求失败，请求参数为null");
        }

        // 查询数据库中对应id的mmd详情数据
        JSON json = pageService.mmdDetailPage(id);

        return ResponseResult.create().ok(json, "/api/public-data/mmd-detail/", ("请求成功，返回mmd-" + id + "页面"));

    }

    @PostMapping("/game-detail")
    @ApiOperation("游戏的详细页面请求api")
    @ApiParam("请求的游戏id -- id")
    @UserLog("访问游戏页面")
    public JSON gameDetailData(@RequestBody Integer id) {
        if (id == null) {
            return ResponseResult.create().ok(null, "/api/public-data/game-detail/", "请求失败，请求参数为null");
        }

        // 查询数据库中对应id的游戏详情数据
        JSON json = pageService.gameDetailPage(id);

        return ResponseResult.create().ok(json, "/api/public-data/game-detail/", ("请求成功，返回游戏" + id + "页面"));

    }

    @PostMapping("/user-home")
    @ApiOperation("访问用户的个人主页页面，判断id是否时访问主页的本人，若不是，则返回部分数据")
    @ApiParam("用户id")
    public JSON userHomeData(@RequestBody Integer id) {
        if (id == null) {
            return ResponseResult.create().ok(null, "/api/public-data/user-home/", "请求失败，请求参数为null");
        }
        JSON json;
        // 获取当前用户登录的id
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(principal.toString())) {
            // 没有用户登录
            json = pageService.userHomeData(id, 0);
        } else {
            // 有用户登录
            json = pageService.userHomeData(id, Integer.valueOf(principal.toString()));
        }

        return ResponseResult.create().ok(json, "/api/public-data/user-home/", "请求成功，返回用户" + id +"主页数据");
    }

    @PostMapping("/search")
    @ApiOperation("搜索api，返回全部模糊匹配text的数据 返回值为map的json字符串，里面有anime，mmd，game三个key值")
    @ApiParam("搜索的文本")
    @UserLog("访问搜索页面")
    public JSON searchData(@RequestBody String text) {
        if (text == null) {
            log.warn("请求参数为空");
            return ResponseResult.create().ok(null, "/api/public-data/search/", ("请求失败，参数为空"));
        }

        JSON json = pageService.searchPage(text);

        return ResponseResult.create().ok(json, "/api/public-data/search/", ("请求成功，返回匹配" + text + "文本的数据"));
    }




}
