package top.doraemonqwq.dora.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.dao.mapper.VideosMapper;
import top.doraemonqwq.dora.entity.pojo.VideoPojo;
import top.doraemonqwq.dora.service.VideosService;
import top.doraemonqwq.dora.utils.RedisUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Doraemon
 * videosService的实体类
 */
@Service("VideosServiceImpl")
@Slf4j
public class VideosServiceImpl implements VideosService {
    @Autowired
    VideosMapper mapper;
    @Autowired
    RedisUtil redisUtil;

    private final String REDIS_USER_VIDEOS_PREFIX = "user_videos_";
    private final String REDIS_ANIME_VIDEOS_PREFIX = "anime_videos_";
    private final String REDIS_MMD_VIDEOS_PREFIX = "mmd_videos_";
    private final String REDIS_VIDEO_PREFIX = "video_";
    private final String VIDEO_ID = "id";
    private final String NAME = "name";
    private final String URL = "url";

    /**
     * 判断缓存中存不存在这个视频，如果存在，那么就删除缓存
     * @param videoId 视频id
     */
    private void inspectRedisAllImagesData(Integer videoId) {
        Set<String> userScan = redisUtil.scan(REDIS_USER_VIDEOS_PREFIX);
        Set<String> animeScan = redisUtil.scan(REDIS_ANIME_VIDEOS_PREFIX);
        Set<String> mmdScan = redisUtil.scan(REDIS_MMD_VIDEOS_PREFIX);
        imagesDataFor(userScan, videoId);
        imagesDataFor(animeScan, videoId);
        imagesDataFor(mmdScan, videoId);
    }

    /**
     * 遍历模糊查询到的keys，然后得到对应缓存，并对比是否拥有指定图片id，如果有就删除
     * @param scanData 模糊查询的keys
     * @param videoId 需要对比的视频id
     */
    private void imagesDataFor(Set<String> scanData, Integer videoId) {
        for (String data : scanData) {
            // 取出当前缓存
            JSON json = (JSON) redisUtil.get(data);
            List<VideoPojo> videoPojos = JSONUtil.parseArray(json.toString()).toList(VideoPojo.class);
            // 遍历缓存中的图片信息
            for (VideoPojo videoPojo : videoPojos) {
                // 判断图片id是否相同
                if (videoPojo.getId().equals(videoId)) {
                    // 删除缓存
                    redisUtil.del(data);
                    break;
                }
            }
        }
    }

    private boolean judgeRedisDataExist(String keyName) {
        return redisUtil.hasKey(keyName);
    }


    /**
     * 通过Anime关联表查询目标id所拥有的视频合集
     *
     * @param animeId 目标id
     * @return 返回拥有的视频合集
     */
    @Override
    public List<VideoPojo> selectVideosAssByAnimeId(Integer animeId) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_ANIME_VIDEOS_PREFIX + animeId)) {
            // 存在，就取出
            JSON json = (JSON) redisUtil.get(REDIS_ANIME_VIDEOS_PREFIX + animeId);
            return JSONUtil.parseArray(json.toString()).toList(VideoPojo.class);
        }
        // 不存在，就查数据库，然后存入redis
        List<VideoPojo> videoPojos = mapper.selectVideosAssByAnimeId(animeId);
        redisUtil.set(REDIS_ANIME_VIDEOS_PREFIX + animeId, JSONUtil.parse(videoPojos));
        return videoPojos;
    }

    /**
     * 通过Mmd关联表查询目标id所拥有的视频合集
     *
     * @param mmdId 目标id
     * @return 返回拥有的视频合集
     */
    @Override
    public List<VideoPojo> selectVideosAssByMmdId(Integer mmdId) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_MMD_VIDEOS_PREFIX + mmdId)) {
            // 存在，就取出
            JSON json = (JSON) redisUtil.get(REDIS_MMD_VIDEOS_PREFIX + mmdId);
            return JSONUtil.parseArray(json.toString()).toList(VideoPojo.class);
        }
        // 不存在，就查数据库，然后存入redis
        List<VideoPojo> videoPojos = mapper.selectVideosAssByAnimeId(mmdId);
        redisUtil.set(REDIS_MMD_VIDEOS_PREFIX + mmdId, JSONUtil.parse(videoPojos));
        return videoPojos;
    }

    /**
     * 通过视频id查询视频的全部信息
     *
     * @param videoId 视频id
     * @return 视频信息
     */
    @Override
    public VideoPojo selectVideoById(Integer videoId) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_VIDEO_PREFIX + videoId)) {
            // 存在，就取出
            JSON json = (JSON) redisUtil.get(REDIS_VIDEO_PREFIX + videoId);
            return json.toBean(VideoPojo.class);
        }
        // 不存在，就查询数据库，并存入redis中
        VideoPojo videoPojo = mapper.selectVideoById(videoId);
        redisUtil.set(REDIS_VIDEO_PREFIX + videoId, JSONUtil.parse(videoPojo));
        return videoPojo;
    }

    /**
     * 添加一个视频
     *
     * @param videoPojo 视频信息
     * @return true为成功，false为失败
     */
    @Override
    public boolean addVideo(VideoPojo videoPojo) {
        if (videoPojo == null) {
            return false;
        }
        return mapper.addVideo(videoPojo) > 0;
    }

    /**
     * 在Anime关联表中添加一个关联信息
     *
     * @param animeId 动漫id
     * @param videoId 视频id
     * @return true为成功，false为失败
     */
    @Override
    public boolean addVideoAnimeAss(Integer animeId, Integer videoId) {
        // 先删除已经有的缓存
        if (judgeRedisDataExist(REDIS_ANIME_VIDEOS_PREFIX + animeId)) {
            redisUtil.del(REDIS_ANIME_VIDEOS_PREFIX + animeId);
        }
        // 添加关系表
        return mapper.addVideoAnimeAss(animeId, videoId) > 0;
    }

    /**
     * 在mmd关联表中添加一个关联信息
     *
     * @param mmdId   mmdId
     * @param videoId 视频id
     * @return true为成功，false为失败
     */
    @Override
    public boolean addVideoMmdAss(Integer mmdId, Integer videoId) {
        // 先删除已经有的缓存
        if (judgeRedisDataExist(REDIS_MMD_VIDEOS_PREFIX + mmdId)) {
            redisUtil.del(REDIS_MMD_VIDEOS_PREFIX + mmdId);
        }
        // 添加关系表
        return mapper.addVideoMmdAss(mmdId, videoId) > 0;
    }

    /**
     * 修改一个视频信息
     *
     * @param videoId 需要修改的视频id
     * @param newName 新的视频名字
     * @return true为成功，false为失败
     */
    @Override
    public boolean updateVideoName(Integer videoId, String newName) {
        if (newName == null) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        inspectRedisAllImagesData(videoId);
        Map<String, Object> map = new HashMap<>(2);
        map.put(VIDEO_ID, videoId);
        map.put(NAME,newName);
        return mapper.updateVideo(map) > 0;
    }

    /**
     * 修改一个视频信息
     *
     * @param videoId 需要修改的视频id
     * @param newUrl  新的视频url
     * @return true为成功，false为失败
     */
    @Override
    public boolean updateVideoUrl(Integer videoId, String newUrl) {
        if (newUrl == null) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        inspectRedisAllImagesData(videoId);
        Map<String, Object> map = new HashMap<>(2);
        map.put(VIDEO_ID, videoId);
        map.put(URL,newUrl);
        return mapper.updateVideo(map) > 0;
    }

    /**
     * 删除一个视频信息
     *
     * @param videoId 视频id
     * @return true为成功，false为失败
     */
    @Override
    public boolean delVideo(Integer videoId) {
        inspectRedisAllImagesData(videoId);
        redisUtil.del(REDIS_VIDEO_PREFIX + videoId);
        return mapper.delVideo(videoId) > 0;
    }
}
