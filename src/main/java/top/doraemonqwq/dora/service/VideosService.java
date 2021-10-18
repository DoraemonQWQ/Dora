package top.doraemonqwq.dora.service;

import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.entity.pojo.VideoPojo;

import java.util.List;

/**
 * @author Doraemon
 * videos的服务接口
 */
@Service
public interface VideosService {

    /**
     * 通过Anime关联表查询目标id所拥有的视频合集
     * @param animeId 目标id
     * @return 返回拥有的视频合集
     */
    List<VideoPojo> selectVideosAssByAnimeId(Integer animeId);

    /**
     * 通过Mmd关联表查询目标id所拥有的视频合集
     * @param mmdId 目标id
     * @return 返回拥有的视频合集
     */
    List<VideoPojo> selectVideosAssByMmdId(Integer mmdId);

    /**
     * 通过视频id查询视频的全部信息
     * @param videoId 视频id
     * @return 视频信息
     */
    VideoPojo selectVideoById(Integer videoId);

    /**
     * 添加一个视频
     * @param videoPojo 视频信息
     * @return true为成功，false为失败
     */
    boolean addVideo(VideoPojo videoPojo);

    /**
     * 在Anime关联表中添加一个关联信息
     * @param animeId 动漫id
     * @param videoId 视频id
     * @return true为成功，false为失败
     */
    boolean addVideoAnimeAss(Integer animeId, Integer videoId);

    /**
     * 在mmd关联表中添加一个关联信息
     * @param mmdId mmdId
     * @param videoId 视频id
     * @return true为成功，false为失败
     */
    boolean addVideoMmdAss(Integer mmdId, Integer videoId);

    /**
     * 修改一个视频信息
     * @param videoId 需要修改的视频id
     * @param newName 新的视频名字
     * @return true为成功，false为失败
     */
    boolean updateVideoName(Integer videoId, String newName);

    /**
     * 修改一个视频信息
     * @param videoId 需要修改的视频id
     * @param newUrl 新的视频url
     * @return true为成功，false为失败
     */
    boolean updateVideoUrl(Integer videoId, String newUrl);

    /**
     * 删除一个视频信息
     * @param videoId 视频id
     * @return true为成功，false为失败
     */
    boolean delVideo(Integer videoId);

}
