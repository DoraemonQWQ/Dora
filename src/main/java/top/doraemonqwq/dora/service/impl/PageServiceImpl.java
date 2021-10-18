package top.doraemonqwq.dora.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.dto.AnimeDTO;
import top.doraemonqwq.dora.dto.GameDTO;
import top.doraemonqwq.dora.dto.MmdDTO;
import top.doraemonqwq.dora.dto.UserHomeDTO;
import top.doraemonqwq.dora.entity.pojo.*;
import top.doraemonqwq.dora.service.*;
import top.doraemonqwq.dora.utils.RedisUtil;

import java.util.*;

/**
 * PageService的实现类
 * @author Doraemon
 */
@Service("PageServiceImpl")
@Slf4j
public class PageServiceImpl implements PageService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    @Qualifier("ImagesServiceImpl")
    ImagesService imagesService;

    @Autowired
    @Qualifier("VideosServiceImpl")
    VideosService videosService;

    @Autowired
    @Qualifier("AnimeServiceImpl")
    AnimeService animeService;

    @Autowired
    @Qualifier("MmdServiceImpl")
    MmdService mmdService;

    @Autowired
    @Qualifier("GameServiceImpl")
    GameService gameService;

    @Autowired
    @Qualifier("UserServiceImpl")
    UserService userService;

    private final String REDIS_ANIME_PREFIX = "anime_";
    private final String REDIS_MMD_PREFIX = "mmd_";
    private final String REDIS_GAME_PREFIX = "game_";
    private final String REDIS_WATCH_SUFFIX = "_watch";
    private final String REDIS_DOWNLOADS_SUFFIX = "_downloads";
    private final String TYPE_ANIME = "anime";
    private final String TYPE_MMD = "mmd";
    private final String TYPE_GAME = "game";


    /**
     * 查询key存不存在
     * @param keyName key的名称
     * @return 存在返回true 不存在返回false
     */
    private boolean judgeRedisDataExist(String keyName) {
        return redisUtil.hasKey(keyName);
    }

    /**
     * 公开资源的数据请求业务
     *
     * @param type 请求的类型
     * @return 返回对应资源
     */
    @Override
    public JSON publicPage(String type) {
        if (Objects.equals(type, TYPE_ANIME)) {
            // 获取所有的资源
            List<Anime> animeList = animeService.selectAnimes();
            // 将得到的集合，转换为dto类型，并且在redis中查找点击数
            ArrayList<AnimeDTO> animeDTOS = new ArrayList<>();
            for (Anime anime : animeList) {
                AnimeDTO animeDTO = new AnimeDTO(anime);
                // 通过anime的id获取图片
                List<ImagePojo> imagePojos = imagesService.selectImagesAssByAnimeId(anime.getId());
                animeDTO.setImages(imagePojos);
                // 通过anime的id获取点击数
                String redisKey = REDIS_ANIME_PREFIX + anime.getId() + REDIS_WATCH_SUFFIX;
                // 判断redis中有没有缓存
                if (judgeRedisDataExist(redisKey)) {
                    // 有就用
                    Integer clickVolume = (Integer) redisUtil.get(redisKey);
                    animeDTO.setClickVolume(clickVolume);
                } else {
                    // 没有就新存入
                    redisUtil.set(redisKey, 0);
                    animeDTO.setClickVolume(0);
                }
                animeDTOS.add(animeDTO);
            }
            return JSONUtil.parse(animeDTOS);
        } else if (Objects.equals(type, TYPE_MMD)) {
            // 获取所有的资源
            List<MmdPojo> mmdPojos = mmdService.selectMmds();
            // 将得到的集合，转换为dto类型，并且在redis中查找点击数
            ArrayList<MmdDTO> mmdDTOS = new ArrayList<>();
            for (MmdPojo mmdPojo : mmdPojos) {
                MmdDTO mmdDTO = new MmdDTO(mmdPojo);
                // 通过anime的id获取图片
                List<ImagePojo> imagePojos = imagesService.selectImagesAssByMmdId(mmdPojo.getId());
                mmdDTO.setImages(imagePojos);
                // 通过anime的id获取点击数
                String redisKey = REDIS_MMD_PREFIX + mmdPojo.getId() + REDIS_WATCH_SUFFIX;
                // 判断redis中有没有缓存
                if (judgeRedisDataExist(redisKey)) {
                    // 有就用
                    Integer clickVolume = (Integer) redisUtil.get(redisKey);
                    mmdDTO.setClickVolume(clickVolume);
                } else {
                    // 没有就新存入
                    redisUtil.set(redisKey, 0);
                    mmdDTO.setClickVolume(0);
                }
                mmdDTOS.add(mmdDTO);
            }
            return JSONUtil.parse(mmdDTOS);
        } else if (Objects.equals(type, TYPE_GAME)) {
            List<GamePojo> gamePojos = gameService.selectGames();
            // 将得到的集合，转换为dto类型，并且在redis中查找点击数
            ArrayList<GameDTO> gameDTOS = new ArrayList<>();
            for (GamePojo gamePojo : gamePojos) {
                GameDTO gameDTO = new GameDTO(gamePojo);
                // 通过anime的id获取图片
                List<ImagePojo> imagePojos = imagesService.selectImagesAssByGameId(gamePojo.getId());
                gameDTO.setImages(imagePojos);
                // 通过anime的id获取点击数
                String redisKey = REDIS_GAME_PREFIX + gamePojo.getId() + REDIS_DOWNLOADS_SUFFIX;
                // 判断redis中有没有缓存
                if (judgeRedisDataExist(redisKey)) {
                    // 有就用
                    Integer clickVolume = (Integer) redisUtil.get(redisKey);
                    gameDTO.setClickVolume(clickVolume);
                } else {
                    // 没有就新存入
                    redisUtil.set(redisKey, 0);
                    gameDTO.setClickVolume(0);
                }
                gameDTOS.add(gameDTO);
            }
            return JSONUtil.parse(gameDTOS);
        } else {
            return null;
        }

    }

    /**
     * 动漫详细页面的数据请求业务
     *
     * @param id 请求的资源id
     * @return 返回对应资源
     */
    @Override
    public JSON animeDetailPage(Integer id) {
        Anime anime = animeService.selectAnimeById(id);
        AnimeDTO animeDTO = new AnimeDTO(anime);
        // 查询动漫的视频
        animeDTO.setImages(imagesService.selectImagesAssByAnimeId(id));
        // 查询动漫的图片
        animeDTO.setVideos(videosService.selectVideosAssByAnimeId(id));
        return JSONUtil.parse(animeDTO);
    }

    /**
     * mmd详细页面的数据请求业务
     *
     * @param id 请求的资源id
     * @return 返回对应资源
     */
    @Override
    public JSON mmdDetailPage(Integer id) {
        MmdPojo mmdPojo = mmdService.selectMmdById(id);
        MmdDTO mmdDTO = new MmdDTO(mmdPojo);
        mmdDTO.setImages(imagesService.selectImagesAssByMmdId(id));
        mmdDTO.setVideos(videosService.selectVideosAssByMmdId(id));
        return JSONUtil.parse(mmdDTO);
    }

    /**
     * 游戏详细页面的数据请求业务
     *
     * @param id 请求的资源id
     * @return 返回对应资源
     */
    @Override
    public JSON gameDetailPage(Integer id) {
        GamePojo gamePojo = gameService.selectGameById(id);
        GameDTO gameDTO = new GameDTO(gamePojo);
        gameDTO.setImages(imagesService.selectImagesAssByGameId(id));
        return JSONUtil.parse(gameDTO);
    }

    /**
     * 搜索页面的数据请求业务
     *
     * @param text 搜索的文本
     * @return 返回对应资源
     */
    @Override
    public JSON searchPage(String text) {

        Map<String, List<Object>> map = new HashMap<>(3);

        List<Anime> animeList = animeService.selectAnimesByFuzzyText(text);
        map.put("anime", Collections.singletonList(animeList));

        List<MmdPojo> mmdPojos = mmdService.selectMmdsByFuzzyText(text);
        map.put("mmd", Collections.singletonList(mmdPojos));

        List<GamePojo> gamePojos = gameService.selectGamesByFuzzyText(text);
        map.put("game", Collections.singletonList(gamePojos));


        return JSONUtil.parse(map);
    }

    /**
     * 根据年份筛选动漫资源并返回给前端
     *
     * @param years 年份
     * @return 筛选后的数据
     */
    @Override
    public JSON animeYearsData(String years) {
        List<Anime> animeList = animeService.selectAnimesByYears(years);
        return JSONUtil.parse(animeList);
    }

    /**
     * 根据用户id返回用户主页数据，同时判断访问的id是否跟传入的id相符，若不符合就返回部分数据
     *
     * @param id 用户的id
     * @param tokenId 访问请求的用户id
     * @return 主页数据
     */
    @Override
    public JSON userHomeData(Integer id, Integer tokenId) {

        User user = userService.selectUser(id);

        if (!id.equals(tokenId)) {
            UserHomeDTO userHomeDTO = new UserHomeDTO();
            userHomeDTO.setName(user.getUsername());
            userHomeDTO.setIntroduction(user.getIntroduction());
            userHomeDTO.setCreationDate(user.getCreationDate());
            userHomeDTO.setLastLoginTime(user.getLastLoginTime());
            userHomeDTO.setImagePojos(imagesService.selectImagesAssByUserId(id));
            return JSONUtil.parse(userHomeDTO);
        }


        UserHomeDTO userHomeDTO = new UserHomeDTO();
        userHomeDTO.setName(user.getUsername());
        userHomeDTO.setIntroduction(user.getIntroduction());
        userHomeDTO.setCreationDate(user.getCreationDate());
        userHomeDTO.setLastLoginTime(user.getLastLoginTime());
        userHomeDTO.setImagePojos(imagesService.selectImagesAssByUserId(id));
        return JSONUtil.parse(userHomeDTO);

    }

    /**
     * 更新点击数
     *
     * @param id   需要更新的id
     * @param type 类型
     * @return 返回最新的点击数
     */
    @Override
    public int updateClick(Integer id, String type) {
        if (Objects.equals(type, TYPE_ANIME)) {
            String key = REDIS_ANIME_PREFIX + id + REDIS_WATCH_SUFFIX;
            // 先读取原先数据
            Integer click = (Integer) redisUtil.get(key);
            // 然后删除原先数据
            redisUtil.del(key);
            // 最后添加新数据
            redisUtil.set(key, click + 1);
            return click + 1;


        } else if (Objects.equals(type, TYPE_MMD)) {
            String key = REDIS_MMD_PREFIX + id + REDIS_WATCH_SUFFIX;
            // 先读
            Integer click = (Integer) redisUtil.get(key);
            // 后删
            redisUtil.del(key);
            // 最后新加
            redisUtil.set(key, click + 1);
            return click + 1;

        } else if (Objects.equals(type, TYPE_GAME)) {
            String key = REDIS_GAME_PREFIX + id + REDIS_DOWNLOADS_SUFFIX;

            Integer click = (Integer) redisUtil.get(key);

            redisUtil.del(key);

            redisUtil.set(key,click + 1);
            return click + 1;

        } else {
            return 0;
        }

    }
}
