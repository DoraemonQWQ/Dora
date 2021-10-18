package top.doraemonqwq.dora.service;

import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.entity.pojo.Anime;

import java.util.List;

/**
 * @author Doraemon
 * Anime的服务接口
 */
@Service
public interface AnimeService {

    /**
     * 查询所有的动漫资源
     * @return 返回全部动漫资源的list
     */
    List<Anime> selectAnimes();

    /**
     * 通过年份查询符合的动漫资源
     * @param years 需要指定筛选的年份
     * @return 返回筛选后的动漫资源的list
     */
    List<Anime> selectAnimesByYears(String years);

    /**
     * 通过模糊查询动漫的名称，进行筛选的动漫资源
     * @param text 需要进行模糊查询的文本
     * @return 返回筛选后的动漫资源的list
     */
    List<Anime> selectAnimesByFuzzyText(String text);

    /**
     * 通过动漫的id查询一个动漫资源
     * @param id 需要查询的id
     * @return 返回查询到的动漫资源
     */
    Anime selectAnimeById(Integer id);

    /**
     * 添加一个动漫资源
     * @param anime 需要添加的动漫的实体类
     * @return 返回boolean，true为成功，false为失败
     */
    boolean addAnime(Anime anime);

    /**
     * 更新一个动漫资源的名字
     * @param animeId 需要更新的id
     * @param newName 新的用户名
     * @return 返回boolean，true为成功，false为失败
     */
    boolean updateAnimeName(Integer animeId, String newName);

    /**
     * 更新一个动漫资源的集数
     * @param animeId 需要更新的id
     * @param newQuanity 新的集数
     * @return 返回boolean，true为成功，false为失败
     */
    boolean updateAnimeQuanity(Integer animeId, Integer newQuanity);

    /**
     * 更新一个动漫资源的地区
     * @param animeId 需要更新的id
     * @param newRegion 新的地区
     * @return 返回boolean，true为成功，false为失败
     */
    boolean updateAnimeRegion(Integer animeId, String newRegion);

    /**
     * 更新一个动漫资源的简介
     * @param animeId 需要更新的id
     * @param newIntroduction 新的简介
     * @return 返回boolean，true为成功，false为失败
     */
    boolean updateAnimeIntroduction(Integer animeId, String newIntroduction);

    /**
     * 更新一个动漫资源的完结状态(就是更新表里的end字段)
     * @param animeId 需要更新的id
     * @param newEnd 更新完结状态
     * @return 返回boolean，true为成功，false为失败
     */
    boolean updateAnimeEnd(Integer animeId, Boolean newEnd);

    /**
     * 更新一个动漫资源的年份
     * @param animeId 需要更新的id
     * @param newYears 新的年份
     * @return 返回boolean，true为成功，false为失败
     */
    boolean updateAnimeYears(Integer animeId, String newYears);

    /**
     * 删除一个动漫资源
     * @param id 需要删除的动漫的id
     * @return 返回boolean，true为成功，false为失败
     */
    boolean delAnime(Integer id);

}
