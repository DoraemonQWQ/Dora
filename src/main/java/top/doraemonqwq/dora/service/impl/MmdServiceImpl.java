package top.doraemonqwq.dora.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.dao.mapper.MmdMapper;
import top.doraemonqwq.dora.entity.pojo.MmdPojo;
import top.doraemonqwq.dora.service.MmdService;
import top.doraemonqwq.dora.utils.RedisUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Doraemon
 * mmdService的实现类
 */
@Service("MmdServiceImpl")
@Slf4j
public class MmdServiceImpl implements MmdService {
    @Autowired
    MmdMapper mapper;
    @Autowired
    RedisUtil redisUtil;

    private final String REDIS_MMD_ALL_NAME = "mmds";
    private final String REDIS_MMD_FUZZY_PREFIX = "mmds_fuzzy_";
    private final String REDIS_MMD_ID_PREFIX = "mmd_";
    private final String ANIME_ID = "id";
    private final String NAME = "name";
    private final String INTRODUCTION = "introduction";

    private void updateRedisAll() {
        // 先删除缓存
        redisUtil.del(REDIS_MMD_ALL_NAME);
        // 查询数据库，写入新缓存
        List<MmdPojo> mmdPojos = mapper.selectMmds();
        redisUtil.set(REDIS_MMD_ALL_NAME, JSONUtil.parse(mmdPojos));
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
     * 查询所有的mmd资源
     *
     * @return 返回全部mmd资源的list
     */
    @Override
    public List<MmdPojo> selectMmds() {
        // 查询redis
        if (judgeRedisDataExist(REDIS_MMD_ALL_NAME)) {
            // 存在
            JSON json = (JSON) redisUtil.get(REDIS_MMD_ALL_NAME);
            return JSONUtil.parseArray(json.toString()).toList(MmdPojo.class);
        }
        // 不存在
        List<MmdPojo> mmdPojos = mapper.selectMmds();
        redisUtil.set(REDIS_MMD_ALL_NAME, JSONUtil.parse(mmdPojos));
        return mmdPojos;
    }

    /**
     * 通过模糊查询mmd的名称，进行筛选的mmd资源
     *
     * @param text 需要进行模糊查询的文本
     * @return 返回筛选后的mmd资源的list
     */
    @Override
    public List<MmdPojo> selectMmdsByFuzzyText(String text) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_MMD_FUZZY_PREFIX + text)) {
            // 存在
            JSON json = (JSON) redisUtil.get(REDIS_MMD_FUZZY_PREFIX + text);
            return JSONUtil.parseArray(json.toString()).toList(MmdPojo.class);
        }
        // 不存在
        List<MmdPojo> mmdPojos = mapper.selectMmdsByFuzzyText(text);
        // 保存redis中，有效时间为7天
        redisUtil.set(REDIS_MMD_FUZZY_PREFIX + text, JSONUtil.parse(mmdPojos), 60 * 60 * 24 * 7);

        return mmdPojos;
    }

    /**
     * 通过mmd的id查询一个mmd资源
     *
     * @param id 需要查询的id
     * @return 返回查询到的mmd资源
     */
    @Override
    public MmdPojo selectMmdById(Integer id) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_MMD_ID_PREFIX + id)) {
            // 存在
            JSON json = (JSON) redisUtil.get(REDIS_MMD_ID_PREFIX + id);
            return json.toBean(MmdPojo.class);
        }
        // 不存在
        MmdPojo mmdPojo = mapper.selectMmdById(id);
        redisUtil.set(REDIS_MMD_ID_PREFIX + id, JSONUtil.parse(mmdPojo));

        return mmdPojo;
    }

    /**
     * 添加一个mmd资源
     *
     * @param mmdPojo 需要添加的mmd的实体类
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean addMmd(MmdPojo mmdPojo) {
        // 添加成功，更新缓存
        if (mapper.addMmd(mmdPojo) > 0) {
            updateRedisAll();
            return true;
        }
        return false;
    }

    /**
     * 更新一个mmd资源
     *
     * @param mmdId   需要更新的id
     * @param newName 新的mmd名字
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean updateMmdName(Integer mmdId, String newName) {
        // 判断需要修改的数据是否为空
        if (newName == null) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        // 删除缓存数据
        redisUtil.del(REDIS_MMD_ID_PREFIX + mmdId);

        Map<String, Object> map = new HashMap<>(2);
        map.put(ANIME_ID, mmdId);
        map.put(NAME, newName);

        // 更新数据库, 返回成功数
        int i = mapper.updateMmd(map);

        return i > 0;
    }

    /**
     * 更新一个mmd资源
     *
     * @param mmdId           更新的内容
     * @param newIntroduction 新的简介
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean updateMmdIntroduction(Integer mmdId, String newIntroduction) {
        // 判断需要修改的数据是否为空
        if (newIntroduction == null) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        // 删除缓存数据
        redisUtil.del(REDIS_MMD_ID_PREFIX + mmdId);

        Map<String, Object> map = new HashMap<>(2);
        map.put(ANIME_ID, mmdId);
        map.put(INTRODUCTION, newIntroduction);

        // 更新数据库, 返回成功数
        int i = mapper.updateMmd(map);

        return i > 0;
    }

    /**
     * 删除一个mmd资源
     *
     * @param id 需要删除的mmd的id
     * @return 返回boolean，true为成功，false为失败
     */
    @Override
    public boolean delMmd(Integer id) {
        // 先删除缓存
        redisUtil.del(REDIS_MMD_ID_PREFIX + id);
        // 再删除数据库，得到成功数
        int i = mapper.delMmd(id);
        return i > 0;
    }
}
