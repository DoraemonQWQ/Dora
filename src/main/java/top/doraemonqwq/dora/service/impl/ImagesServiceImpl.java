package top.doraemonqwq.dora.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.dao.mapper.ImagesMapper;
import top.doraemonqwq.dora.entity.pojo.ImagePojo;
import top.doraemonqwq.dora.service.ImagesService;
import top.doraemonqwq.dora.utils.RedisUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Doraemon
 * imagesService的实现类
 */
@Service("ImagesServiceImpl")
@Slf4j
public class ImagesServiceImpl implements ImagesService {
    @Autowired
    ImagesMapper mapper;
    @Autowired
    RedisUtil redisUtil;

    private final String REDIS_USER_IMAGES_PREFIX = "user_images_";
    private final String REDIS_ANIME_IMAGES_PREFIX = "anime_images_";
    private final String REDIS_MMD_IMAGES_PREFIX = "mmd_images_";
    private final String REDIS_GAME_IMAGES_PREFIX = "game_images_";
    private final String REDIS_IMAGE_PREFIX = "image_";
    private final String REDIS_DEFAULT_AVATAR_NAME = "default_avatar";
    private final String IMAGE_ID = "id";
    private final String NAME = "name";
    private final String URL = "url";

    /**
     * 判断缓存中存不存在这个图片，如果存在，那么就删除缓存
     * @param imageId 图片id
     */
    private void inspectRedisAllImagesData(Integer imageId) {
        Set<String> userScan = redisUtil.scan(REDIS_USER_IMAGES_PREFIX);
        Set<String> animeScan = redisUtil.scan(REDIS_ANIME_IMAGES_PREFIX);
        Set<String> mmdScan = redisUtil.scan(REDIS_MMD_IMAGES_PREFIX);
        Set<String> gameScan = redisUtil.scan(REDIS_GAME_IMAGES_PREFIX);
        imagesDataFor(userScan, imageId);
        imagesDataFor(animeScan, imageId);
        imagesDataFor(mmdScan, imageId);
        imagesDataFor(gameScan, imageId);
    }

    /**
     * 遍历模糊查询到的keys，然后得到对应缓存，并对比是否拥有指定图片id，如果有就删除
     * @param scanData 模糊查询的keys
     * @param imageId 需要对比的图片id
     */
    private void imagesDataFor(Set<String> scanData, Integer imageId) {
        for (String data : scanData) {
            // 取出当前缓存
            JSON json = (JSON) redisUtil.get(data);
            List<ImagePojo> imagePojos = JSONUtil.parseArray(json.toString()).toList(ImagePojo.class);
            // 遍历缓存中的图片信息
            for (ImagePojo imagePojo : imagePojos) {
                // 判断图片id是否相同
                if (imagePojo.getId().equals(imageId)) {
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
     * 通过User关联表查询目标id所拥有的图片合集
     *
     * @param userId 目标id
     * @return 返回拥有的图片合集
     */
    @Override
    public List<ImagePojo> selectImagesAssByUserId(Integer userId) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_USER_IMAGES_PREFIX + userId)) {
            // 存在，就取出
            JSON json = (JSON) redisUtil.get(REDIS_USER_IMAGES_PREFIX + userId);
            return JSONUtil.parseArray(json.toString()).toList(ImagePojo.class);
        }
        // 不存在，就查数据库，然后存入redis
        List<ImagePojo> imagePojos = mapper.selectImagesAssByUserId(userId);
        redisUtil.set(REDIS_USER_IMAGES_PREFIX + userId, JSONUtil.parse(imagePojos));
        return imagePojos;
    }

    /**
     * 通过Anime关联表查询目标id所拥有的图片合集
     *
     * @param animeId 目标id
     * @return 返回拥有的图片合集
     */
    @Override
    public List<ImagePojo> selectImagesAssByAnimeId(Integer animeId) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_ANIME_IMAGES_PREFIX + animeId)) {
            // 存在，就取出
            JSON json = (JSON) redisUtil.get(REDIS_ANIME_IMAGES_PREFIX + animeId);
            return JSONUtil.parseArray(json.toString()).toList(ImagePojo.class);
        }
        // 不存在，就查数据库，然后存入redis
        List<ImagePojo> imagePojos = mapper.selectImagesAssByAnimeId(animeId);
        redisUtil.set(REDIS_ANIME_IMAGES_PREFIX + animeId, JSONUtil.parse(imagePojos));
        return imagePojos;
    }

    /**
     * 通过Mmd关联表查询目标id所拥有的图片合集
     *
     * @param mmdId 目标id
     * @return 返回拥有的图片合集
     */
    @Override
    public List<ImagePojo> selectImagesAssByMmdId(Integer mmdId) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_MMD_IMAGES_PREFIX + mmdId)) {
            // 存在，就取出
            JSON json = (JSON) redisUtil.get(REDIS_MMD_IMAGES_PREFIX + mmdId);
            return JSONUtil.parseArray(json.toString()).toList(ImagePojo.class);
        }
        // 不存在，就查数据库，然后存入redis
        List<ImagePojo> imagePojos = mapper.selectImagesAssByMmdId(mmdId);
        redisUtil.set(REDIS_MMD_IMAGES_PREFIX + mmdId, JSONUtil.parse(imagePojos));
        return imagePojos;
    }

    /**
     * 通过Game关联表查询目标id所拥有的图片合集
     *
     * @param gameId 目标id
     * @return 返回拥有的图片合集
     */
    @Override
    public List<ImagePojo> selectImagesAssByGameId(Integer gameId) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_GAME_IMAGES_PREFIX + gameId)) {
            // 存在，就取出
            JSON json = (JSON) redisUtil.get(REDIS_GAME_IMAGES_PREFIX + gameId);
            return JSONUtil.parseArray(json.toString()).toList(ImagePojo.class);
        }
        // 不存在，就查数据库，然后存入redis
        List<ImagePojo> imagePojos = mapper.selectImagesAssByGameId(gameId);
        redisUtil.set(REDIS_GAME_IMAGES_PREFIX + gameId, JSONUtil.parse(imagePojos));
        return imagePojos;
    }

    /**
     * 通过图片id查询图片的全部信息
     *
     * @param imageId 图片id
     * @return 图片信息
     */
    @Override
    public ImagePojo selectImageById(Integer imageId) {
        // 查询redis
        if (judgeRedisDataExist(REDIS_IMAGE_PREFIX + imageId)) {
            // 存在，就取出
            JSON json = (JSON) redisUtil.get(REDIS_IMAGE_PREFIX + imageId);
            return json.toBean(ImagePojo.class);
        }
        // 不存在，就查询数据库，并存入redis中
        ImagePojo imagePojo = mapper.selectImageById(imageId);
        redisUtil.set(REDIS_IMAGE_PREFIX + imageId, JSONUtil.parse(imagePojo));
        return imagePojo;
    }

    /**
     * 通过图片name查询图片的全部信息
     *
     * @param imageName 图片名称
     * @return 图片信息
     */
    @Override
    public int selectImageByName(String imageName) {
        if (judgeRedisDataExist(REDIS_DEFAULT_AVATAR_NAME)) {
            return (int) redisUtil.get(REDIS_DEFAULT_AVATAR_NAME);
        }
        int id = mapper.selectImageByName(imageName);
        redisUtil.set(REDIS_DEFAULT_AVATAR_NAME, id);
        return id;
    }

    /**
     * 添加一个图片
     *
     * @param imagePojo 图片信息
     * @return true为成功，false为失败
     */
    @Override
    public boolean addImage(ImagePojo imagePojo) {
        if (imagePojo == null) {
            return false;
        }
        return mapper.addImage(imagePojo) > 0;
    }

    /**
     * 在User关联表中添加一个关联信息
     *
     * @param userId  用户id
     * @param imageId 图片id
     * @return true为成功，false为失败
     */
    @Override
    public boolean addImageUserAss(Integer userId, Integer imageId) {
        // 先删除已经有的缓存
        if (judgeRedisDataExist(REDIS_USER_IMAGES_PREFIX + userId)) {
            redisUtil.del(REDIS_USER_IMAGES_PREFIX + userId);
        }
        // 添加关系表
        return mapper.addImageUserAss(userId, imageId) > 0;
    }

    /**
     * 在Anime关联表中添加一个关联信息
     *
     * @param animeId 动漫id
     * @param imageId 图片id
     * @return true为成功，false为失败
     */
    @Override
    public boolean addImageAnimeAss(Integer animeId, Integer imageId) {
        // 先删除已经有的缓存
        if (judgeRedisDataExist(REDIS_ANIME_IMAGES_PREFIX + animeId)) {
            redisUtil.del(REDIS_ANIME_IMAGES_PREFIX + animeId);
        }
        // 添加关系表
        return mapper.addImageAnimeAss(animeId, imageId) > 0;
    }

    /**
     * 在mmd关联表中添加一个关联信息
     *
     * @param mmdId   mmdId
     * @param imageId 图片id
     * @return true为成功，false为失败
     */
    @Override
    public boolean addImageMmdAss(Integer mmdId, Integer imageId) {
        // 先删除已经有的缓存
        if (judgeRedisDataExist(REDIS_MMD_IMAGES_PREFIX + mmdId)) {
            redisUtil.del(REDIS_MMD_IMAGES_PREFIX + mmdId);
        }
        // 添加关系表
        return mapper.addImageMmdAss(mmdId, imageId) > 0;
    }

    /**
     * 在game关联表中添加一个关联信息
     *
     * @param gameId   mmdId
     * @param imageId 图片id
     * @return true为成功，false为失败
     */
    @Override
    public boolean addImageGameAss(Integer gameId, Integer imageId) {
        // 先删除已经有的缓存
        if (judgeRedisDataExist(REDIS_GAME_IMAGES_PREFIX + gameId)) {
            redisUtil.del(REDIS_GAME_IMAGES_PREFIX + gameId);
        }
        // 添加关系表
        return mapper.addImageGameAss(gameId, imageId) > 0;
    }

    /**
     * 修改一个图片信息
     *
     * @param imageId 需要更新的图片id
     * @param newName 新图片名字
     * @return true为成功，false为失败
     */
    @Override
    public boolean updateImageName(Integer imageId, String newName) {
        if (newName == null) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        inspectRedisAllImagesData(imageId);
        Map<String, Object> map = new HashMap<>(2);
        map.put(IMAGE_ID, imageId);
        map.put(NAME,newName);
        return mapper.updateImage(map) > 0;
    }

    /**
     * 修改一个图片信息
     *
     * @param imageId 需要更新的图片id
     * @param newUrl  新图片url
     * @return true为成功，false为失败
     */
    @Override
    public boolean updateImageUrl(Integer imageId, String newUrl) {
        if (newUrl == null) {
            log.error("需要修改的数据为空；" + Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        inspectRedisAllImagesData(imageId);
        Map<String, Object> map = new HashMap<>(2);
        map.put(IMAGE_ID, imageId);
        map.put(URL,newUrl);
        return mapper.updateImage(map) > 0;
    }

    /**
     * 删除一个图片信息
     *
     * @param imageId 图片id
     * @return true为成功，false为失败
     */
    @Override
    public boolean delImage(Integer imageId) {
        inspectRedisAllImagesData(imageId);
        redisUtil.del(REDIS_IMAGE_PREFIX + imageId);
        return mapper.delImage(imageId) > 0;
    }
}
