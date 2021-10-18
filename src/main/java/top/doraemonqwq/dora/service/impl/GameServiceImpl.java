package top.doraemonqwq.dora.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.dao.mapper.GameMapper;
import top.doraemonqwq.dora.entity.pojo.GamePojo;
import top.doraemonqwq.dora.service.GameService;
import top.doraemonqwq.dora.utils.RedisUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * gameService的实现类
 * @author Doraemon
 */
@Service("GameServiceImpl")
@Slf4j
public class GameServiceImpl implements GameService {

    @Autowired
    GameMapper mapper;
    @Autowired
    RedisUtil redisUtil;

    private final String REDIS_GAME_ALL_NAME = "games";
    private final String REDIS_GAME_FUZZY_PREFIX = "games_fuzzy_";
    private final String REDIS_GAME_ID_PREFIX = "game_";
    private final String ANIME_ID = "id";
    private final String NAME = "name";
    private final String INTRODUCTION = "introduction";

    private void updateRedisAll() {
        // 先删除缓存
        redisUtil.del(REDIS_GAME_ALL_NAME);
        // 查询数据库，写入新缓存
        List<GamePojo> gamePojos = mapper.selectGames();
        redisUtil.set(REDIS_GAME_ALL_NAME, JSONUtil.parse(gamePojos));
    }

    /**
     * 判断redis中存不存在key
     * @param keyName key的名称
     * @return 存在为true，不存在为false
     */
    private boolean judgeRedisDataExist(String keyName) {
        return redisUtil.hasKey(keyName);
    }

    /**
     * 查询所有的游戏资源
     *
     * @return 返回全部游戏资源的list
     */
    @Override
    public List<GamePojo> selectGames() {
        // 查询redis
        if (judgeRedisDataExist(REDIS_GAME_ALL_NAME)) {
            // 存在
            JSON json = (JSON) redisUtil.get(REDIS_GAME_ALL_NAME);
            return JSONUtil.parseArray(json.toString()).toList(GamePojo.class);
        }
        // 不存在
        List<GamePojo> gamePojos = mapper.selectGames();
        redisUtil.set(REDIS_GAME_ALL_NAME, JSONUtil.parse(gamePojos));
        return gamePojos;
    }

    /**
     * 通过模糊查询游戏的名称，进行筛选的游戏资源
     *
     * @param text 需要进行模糊查询的文本
     * @return 返回筛选后的游戏资源的list
     */
    @Override
    public List<GamePojo> selectGamesByFuzzyText(String text) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_GAME_FUZZY_PREFIX + text)) {
            // 存在
            JSON json = (JSON) redisUtil.get(REDIS_GAME_FUZZY_PREFIX + text);
            return JSONUtil.parseArray(json.toString()).toList(GamePojo.class);
        }
        // 不存在
        List<GamePojo> gamePojos = mapper.selectGamesByFuzzyText(text);
        // 保存redis中，有效时间为7天
        redisUtil.set(REDIS_GAME_FUZZY_PREFIX + text, JSONUtil.parse(gamePojos), 60 * 60 * 24 * 7);

        return gamePojos;
    }

    /**
     * 通过游戏的id查询一个mmd资源
     *
     * @param id 需要查询的id
     * @return 返回查询到的游戏资源
     */
    @Override
    public GamePojo selectGameById(Integer id) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_GAME_ID_PREFIX + id)) {
            // 存在
            JSON json = (JSON) redisUtil.get(REDIS_GAME_ID_PREFIX + id);
            return json.toBean(GamePojo.class);
        }
        // 不存在
        GamePojo gamePojo = mapper.selectGameById(id);
        redisUtil.set(REDIS_GAME_ID_PREFIX + id, JSONUtil.parse(gamePojo));

        return gamePojo;
    }

    /**
     * 添加一个游戏资源
     *
     * @param gamePojo 需要添加的游戏的实体类
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean addGame(GamePojo gamePojo) {
        // 添加成功，更新缓存
        if (mapper.addGame(gamePojo) > 0) {
            updateRedisAll();
            return true;
        }
        return false;
    }

    /**
     * 更新一个游戏资源
     *
     * @param mmdId   需要更新的id
     * @param newName 新的mmd名字
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean updateGameName(Integer mmdId, String newName) {
        // 判断需要修改的数据是否为空
        if (newName == null) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        // 删除缓存数据
        redisUtil.del(REDIS_GAME_ID_PREFIX + mmdId);

        Map<String, Object> map = new HashMap<>(2);
        map.put(ANIME_ID, mmdId);
        map.put(NAME, newName);

        // 更新数据库, 返回成功数
        int i = mapper.updateGame(map);

        return i > 0;
    }

    /**
     * 更新一个游戏资源
     *
     * @param mmdId           更新的内容
     * @param newIntroduction 新的简介
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean updateGameIntroduction(Integer mmdId, String newIntroduction) {
        // 判断需要修改的数据是否为空
        if (newIntroduction == null) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        // 删除缓存数据
        redisUtil.del(REDIS_GAME_ID_PREFIX + mmdId);

        Map<String, Object> map = new HashMap<>(2);
        map.put(ANIME_ID, mmdId);
        map.put(INTRODUCTION, newIntroduction);

        // 更新数据库, 返回成功数
        int i = mapper.updateGame(map);

        return i > 0;
    }

    /**
     * 删除一个游戏资源
     *
     * @param id 需要删除的游戏的id
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean delGame(Integer id) {
        // 先删除缓存
        redisUtil.del(REDIS_GAME_ID_PREFIX + id);
        // 再删除数据库，得到成功数
        int i = mapper.delGame(id);
        return i > 0;
    }

}
