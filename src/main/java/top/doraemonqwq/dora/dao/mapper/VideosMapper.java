package top.doraemonqwq.dora.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.doraemonqwq.dora.entity.pojo.VideoPojo;

import java.util.List;
import java.util.Map;

/**
 * @author Doraemon
 * video表的Mapper接口
 */
@Mapper
@Repository
public interface VideosMapper {

    /**
     * 通过Anime关联表查询目标id所拥有的视频合集
     * @param animeId 目标id
     * @return 返回拥有的视频合集
     */
    List<VideoPojo> selectVideosAssByAnimeId(@Param("anime_id") Integer animeId);

    /**
     * 通过Mmd关联表查询目标id所拥有的视频合集
     * @param mmdId 目标id
     * @return 返回拥有的视频合集
     */
    List<VideoPojo> selectVideosAssByMmdId(@Param("mmd_id") Integer mmdId);

    /**
     * 通过视频id查询视频的全部信息
     * @param videoId 视频id
     * @return 视频信息
     */
    VideoPojo selectVideoById(@Param("video_id") Integer videoId);

    /**
     * 添加一个视频
     * @param videoPojo 视频信息
     * @return 成功数
     */
    int addVideo(VideoPojo videoPojo);

    /**
     * 在Anime关联表中添加一个关联信息
     * @param animeId 动漫id
     * @param videoId 视频id
     * @return 成功数
     */
    int addVideoAnimeAss(@Param("anime_id") Integer animeId, @Param("video_id") Integer videoId);

    /**
     * 在mmd关联表中添加一个关联信息
     * @param mmdId mmdId
     * @param videoId 视频id
     * @return 成功数
     */
    int addVideoMmdAss(@Param("mmd_id") Integer mmdId, @Param("video_id") Integer videoId);

    /**
     * 修改一个视频信息
     * @param map 新的视频信息
     * @return 成功数
     */
    int updateVideo(Map<String,Object> map);

    /**
     * 删除一个视频信息
     * @param videoId 视频id
     * @return 成功数
     */
    int delVideo(@Param("video_id") Integer videoId);
}
