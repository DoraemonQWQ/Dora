package top.doraemonqwq.dora.service;

import cn.hutool.json.JSON;
import org.springframework.stereotype.Service;

/**
 * 页面数据请求业务类
 * @author Doraemon
 */
@Service
public interface PageService {

    /**
     * 公开资源的数据请求业务
     * @param type 请求的类型
     * @return 返回对应资源
     */
    JSON publicPage(String type);

    /**
     * 动漫详细页面的数据请求业务
     * @param id 请求的资源id
     * @return 返回对应资源
     */
    JSON animeDetailPage(Integer id);

    /**
     * mmd详细页面的数据请求业务
     * @param id 请求的资源id
     * @return 返回对应资源
     */
    JSON mmdDetailPage(Integer id);

    /**
     * 游戏详细页面的数据请求业务
     * @param id 请求的资源id
     * @return 返回对应资源
     */
    JSON gameDetailPage(Integer id);

    /**
     * 根据年份筛选动漫资源并返回给前端
     * @param years 年份
     * @return 筛选后的数据
     */
    JSON animeYearsData(String years);

    /**
     * 根据用户id返回用户主页数据，同时判断访问的id是否跟传入的id相符，若不符合就返回部分数据
     * @param id 用户的id
     * @param tokenId 访问请求的用户id
     * @return 主页数据
     */
    JSON userHomeData(Integer id, Integer tokenId);

    /**
     * 动漫详细页面的数据请求业务
     * @param text 搜索的文本
     * @return 返回对应资源
     */
    JSON searchPage(String text);

    /**
     * 更新点击数
     * @param id 需要更新的id
     * @param type 类型
     * @return 返回最新的点击数
     */
    int updateClick(Integer id, String type);

}
