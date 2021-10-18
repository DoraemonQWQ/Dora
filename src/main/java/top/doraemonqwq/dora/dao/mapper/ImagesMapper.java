package top.doraemonqwq.dora.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.doraemonqwq.dora.entity.pojo.ImagePojo;

import java.util.List;
import java.util.Map;

/**
 * @author Doraemon
 * images表的Mapper接口
 */
@Mapper
@Repository
public interface ImagesMapper {

    /**
     * 通过User关联表查询目标id所拥有的图片合集
     * @param userId 目标id
     * @return 返回拥有的图片合集
     */
    List<ImagePojo> selectImagesAssByUserId(@Param("user_id") Integer userId);

    /**
     * 通过Anime关联表查询目标id所拥有的图片合集
     * @param animeId 目标id
     * @return 返回拥有的图片合集
     */
    List<ImagePojo> selectImagesAssByAnimeId(@Param("anime_id") Integer animeId);

    /**
     * 通过Mmd关联表查询目标id所拥有的图片合集
     * @param mmdId 目标id
     * @return 返回拥有的图片合集
     */
    List<ImagePojo> selectImagesAssByMmdId(@Param("mmd_id") Integer mmdId);

    /**
     * 通过Mmd关联表查询目标id所拥有的图片合集
     * @param gameId 目标id
     * @return 返回拥有的图片合集
     */
    List<ImagePojo> selectImagesAssByGameId(@Param("game_id") Integer gameId);

    /**
     * 通过图片id查询图片的全部信息
     * @param imageId 图片id
     * @return 图片信息
     */
    ImagePojo selectImageById(@Param("image_id") Integer imageId);

    /**
     * 通过图片id查询图片的全部信息
     * @param imageName 图片名称
     * @return 图片id
     */
    int selectImageByName(@Param("image_name") String imageName);

    /**
     * 添加一个图片
     * @param imagePojo 图片信息
     * @return 成功数
     */
    int addImage(ImagePojo imagePojo);

    /**
     * 在User关联表中添加一个关联信息
     * @param userId 用户id
     * @param imageId 图片id
     * @return 成功数
     */
    int addImageUserAss(@Param("user_id") Integer userId, @Param("image_id") Integer imageId);

    /**
     * 在Anime关联表中添加一个关联信息
     * @param animeId 动漫id
     * @param imageId 图片id
     * @return 成功数
     */
    int addImageAnimeAss(@Param("anime_id") Integer animeId, @Param("image_id") Integer imageId);

    /**
     * 在mmd关联表中添加一个关联信息
     * @param mmdId mmdId
     * @param imageId 图片id
     * @return 成功数
     */
    int addImageMmdAss(@Param("mmd_id") Integer mmdId, @Param("image_id") Integer imageId);

    /**
     * 在mmd关联表中添加一个关联信息
     * @param gameId mmdId
     * @param imageId 图片id
     * @return 成功数
     */
    int addImageGameAss(@Param("mmd_id") Integer gameId, @Param("image_id") Integer imageId);

    /**
     * 修改一个图片信息
     * @param map 新的图片信息
     * @return 成功数
     */
    int updateImage(Map<String,Object> map);

    /**
     * 删除一个图片信息
     * @param imageId 图片id
     * @return 成功数
     */
    int delImage(@Param("image_id") Integer imageId);
}
