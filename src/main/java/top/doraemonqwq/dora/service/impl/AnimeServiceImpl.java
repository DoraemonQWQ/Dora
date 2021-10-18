package top.doraemonqwq.dora.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.dao.mapper.AnimeMapper;
import top.doraemonqwq.dora.entity.pojo.Anime;
import top.doraemonqwq.dora.service.AnimeService;
import top.doraemonqwq.dora.utils.RedisUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Doraemon
 * AnimeService的实现类
 */
@Service("AnimeServiceImpl")
public class AnimeServiceImpl implements AnimeService {

    @Autowired
    AnimeMapper animeMapper;
    @Autowired
    RedisUtil redisUtil;

    private final String REDIS_ANIME_ALL_NAME = "animes";
    private final String REDIS_ANIME_YEARS_NAME = "animes_years";
    private final String REDIS_ANIME_FUZZY_PREFIX = "animes_fuzzy_";
    private final String REDIS_ANIME_ID_PREFIX = "anime_";
    private final String ANIME_ID = "id";
    private final String NAME = "name";
    private final String QUANITY = "quanity";
    private final String REGION = "region";
    private final String INTRODUCTION = "introduction";
    private final String END = "END";
    private final String YEARS = "years";

    private void updateRedisAll() {
        // 先删除缓存
        redisUtil.del(REDIS_ANIME_ALL_NAME);
        redisUtil.del(REDIS_ANIME_YEARS_NAME);
        List<Anime> animeList = animeMapper.selectAnimes();
        redisUtil.set(REDIS_ANIME_ALL_NAME, JSONUtil.parse(animeList));
    }

    private boolean judgeRedisDataExist(String keyName) {
        return redisUtil.hasKey(keyName);
    }

    /**
     * 查询所有的动漫资源
     *
     * @return 返回全部动漫资源的list
     */
    @Override
    public List<Anime> selectAnimes() {
        // 先判断redis中有没有数据
        if (judgeRedisDataExist(REDIS_ANIME_ALL_NAME)) {
            // 如果有，那么直接从缓存中拿取
            JSON json = (JSON) redisUtil.get(REDIS_ANIME_ALL_NAME);
            return JSONUtil.parseArray(json.toString()).toList(Anime.class);
        }
        // 如果没有，那么从数据库中查询，并放入redis中
        List<Anime> animeList = animeMapper.selectAnimes();
        redisUtil.set(REDIS_ANIME_ALL_NAME, JSONUtil.parse(animeList));
        return animeList;
    }

    /**
     * 通过年份查询符合的动漫资源
     *
     * @param years 需要指定筛选的年份
     * @return 返回筛选后的动漫资源的list
     */
    @Override
    public List<Anime> selectAnimesByYears(String years) {
        // 查询redis中存不存在数据
        if (judgeRedisDataExist(REDIS_ANIME_YEARS_NAME)) {
            // 如果存在，那么就从redis中取出
            JSON json = (JSON) redisUtil.get(REDIS_ANIME_YEARS_NAME);
            return JSONUtil.parseArray(json.toString()).toList(Anime.class);
        }
        // 如果不存在，那么从数据库中取出，然后放进redis中，并设置7天的有效时间
        List<Anime> animeList = animeMapper.selectAnimesByYears(years);
        redisUtil.set(REDIS_ANIME_YEARS_NAME, animeList, 60 * 60 * 24 * 7);
        return animeList;

    }

    /**
     * 通过模糊查询动漫的名称，进行筛选的动漫资源
     *
     * @param text 需要进行模糊查询的文本
     * @return 返回筛选后的动漫资源的list
     */
    @Override
    public List<Anime> selectAnimesByFuzzyText(String text) {
        // 查询redis中存不存在
        if (judgeRedisDataExist(REDIS_ANIME_FUZZY_PREFIX + text)) {
            // 如果存在，那么从redis中取出
            JSON json = (JSON) redisUtil.get(REDIS_ANIME_FUZZY_PREFIX + text);
            return JSONUtil.parseArray(json.toString()).toList(Anime.class);
        }
        // 如果不存在，那么从数据库中取出，并存入redis中，有效时间为24小时
        List<Anime> animeList = animeMapper.selectAnimesByFuzzyText(text);
        redisUtil.set(REDIS_ANIME_FUZZY_PREFIX + text, animeList, 60 * 60 * 24);
        return animeList;
    }

    /**
     * 通过动漫的id查询一个动漫资源
     *
     * @param id 需要查询的id
     * @return 返回查询到的动漫资源
     */
    @Override
    public Anime selectAnimeById(Integer id) {
        // 查询redis
        if (redisUtil.hasKey(REDIS_ANIME_ID_PREFIX + id)) {
            // 存在
            JSON json = (JSON) redisUtil.get(REDIS_ANIME_ID_PREFIX + id);
            return json.toBean(Anime.class);
        }
        // 不存在
        Anime anime = animeMapper.selectAnimeById(id);
        redisUtil.set(REDIS_ANIME_ID_PREFIX + id, JSONUtil.parse(anime));
        return anime;
    }

    /**
     * 添加一个动漫资源
     *
     * @param anime 需要添加的动漫的实体类
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean addAnime(Anime anime) {
        // 先添加
        if (animeMapper.addAnime(anime) > 0) {
            // 添加成功后，更新缓存
            updateRedisAll();
            return true;
        }
        // 添加失败，直接返回false
        return false;
    }

    /**
     * 更新一个动漫资源的名字
     *
     * @param animeId 需要更新的id
     * @param newName 新的用户名
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean updateAnimeName(Integer animeId, String newName) {
        // 判断需要修改的数据是否为空
        if (newName == null) {
            throw new RuntimeException("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        // 删除缓存数据
        redisUtil.del(REDIS_ANIME_ID_PREFIX + animeId);

        Map<String, Object> map = new HashMap<>(2);
        map.put(ANIME_ID, animeId);
        map.put(NAME, newName);

        // 更新数据库, 返回成功数
        int i = animeMapper.updateAnime(map);

        return i > 0;
    }

    /**
     * 更新一个动漫资源的集数
     *
     * @param animeId    需要更新的id
     * @param newQuanity 新的集数
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean updateAnimeQuanity(Integer animeId, Integer newQuanity) {
        // 判断需要修改的数据是否为空
        if (newQuanity == null) {
            throw new RuntimeException("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        // 删除缓存数据
        redisUtil.del(REDIS_ANIME_ID_PREFIX + animeId);

        Map<String, Object> map = new HashMap<>(2);
        map.put(ANIME_ID, animeId);
        map.put(QUANITY, newQuanity);

        // 更新数据库, 返回成功数
        int i = animeMapper.updateAnime(map);

        return i > 0;
    }

    /**
     * 更新一个动漫资源的地区
     *
     * @param animeId   需要更新的id
     * @param newRegion 新的地区
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean updateAnimeRegion(Integer animeId, String newRegion) {
        // 判断需要修改的数据是否为空
        if (newRegion == null) {
            throw new RuntimeException("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        // 删除缓存数据
        redisUtil.del(REDIS_ANIME_ID_PREFIX + animeId);

        Map<String, Object> map = new HashMap<>(2);
        map.put(ANIME_ID, animeId);
        map.put(REGION, newRegion);

        // 更新数据库, 返回成功数
        int i = animeMapper.updateAnime(map);

        return i > 0;
    }

    /**
     * 更新一个动漫资源的简介
     *
     * @param animeId         需要更新的id
     * @param newIntroduction 新的简介
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean updateAnimeIntroduction(Integer animeId, String newIntroduction) {
        // 判断需要修改的数据是否为空
        if (newIntroduction == null) {
            throw new RuntimeException("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        // 删除缓存数据
        redisUtil.del(REDIS_ANIME_ID_PREFIX + animeId);

        Map<String, Object> map = new HashMap<>(2);
        map.put(ANIME_ID, animeId);
        map.put(INTRODUCTION, newIntroduction);

        // 更新数据库, 返回成功数
        int i = animeMapper.updateAnime(map);

        return i > 0;
    }

    /**
     * 更新一个动漫资源的完结状态(就是更新表里的end字段)
     *
     * @param animeId 需要更新的id
     * @param newEnd  更新完结状态
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean updateAnimeEnd(Integer animeId, Boolean newEnd) {
        // 判断需要修改的数据是否为空
        if (newEnd == null) {
            throw new RuntimeException("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        // 删除缓存数据
        redisUtil.del(REDIS_ANIME_ID_PREFIX + animeId);

        Map<String, Object> map = new HashMap<>(2);
        map.put(ANIME_ID, animeId);
        map.put(END, newEnd);

        // 更新数据库, 返回成功数
        int i = animeMapper.updateAnime(map);

        return i > 0;
    }

    /**
     * 更新一个动漫资源的年份
     *
     * @param animeId  需要更新的id
     * @param newYears 新的年份
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean updateAnimeYears(Integer animeId, String newYears) {
        // 判断需要修改的数据是否为空
        if (newYears == null) {
            throw new RuntimeException("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        // 删除缓存数据
        redisUtil.del(REDIS_ANIME_ID_PREFIX + animeId);

        Map<String, Object> map = new HashMap<>(2);
        map.put(ANIME_ID, animeId);
        map.put(YEARS, newYears);

        // 更新数据库, 返回成功数
        int i = animeMapper.updateAnime(map);

        return i > 0;
    }

    /**
     * 删除一个动漫资源
     *
     * @param id 需要删除的动漫的id
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean delAnime(Integer id) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_ANIME_ID_PREFIX + id)) {
            // 存在，就删除
            redisUtil.del(REDIS_ANIME_ID_PREFIX + id);
        }
        // 操作数据库，返回成功数
        int i = animeMapper.delAnime(id);
        // 成功数大于0则成功
        return i > 0;
    }
}
