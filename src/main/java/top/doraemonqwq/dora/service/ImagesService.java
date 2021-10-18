package top.doraemonqwq.dora.service;

import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.entity.pojo.ImagePojo;

import java.util.List;

/**
 * @author Doraemon
 * images的服务接口
 */
@Service
public interface ImagesService {

    /**
     * 通过User关联表查询目标id所拥有的图片合集
     * @param userId 目标id
     * @return 返回拥有的图片合集
     */
    List<ImagePojo> selectImagesAssByUserId(Integer userId);

    /**
     * 通过Anime关联表查询目标id所拥有的图片合集
     * @param animeId 目标id
     * @return 返回拥有的图片合集
     */
    List<ImagePojo> selectImagesAssByAnimeId(Integer animeId);

    /**
     * 通过Mmd关联表查询目标id所拥有的图片合集
     * @param mmdId 目标id
     * @return 返回拥有的图片合集
     */
    List<ImagePojo> selectImagesAssByMmdId(Integer mmdId);

    /**
     * 通过Game关联表查询目标id所拥有的图片合集
     * @param gameId 目标id
     * @return 返回拥有的图片合集
     */
    List<ImagePojo> selectImagesAssByGameId(Integer gameId);

    /**
     * 通过图片id查询图片的全部信息
     * @param imageId 图片id
     * @return 图片信息
     */
    ImagePojo selectImageById(Integer imageId);

    /**
     * 通过图片name查询图片的全部信息
     * @param imageName 图片名称
     * @return 图片信息
     */
    int selectImageByName(String imageName);

    /**
     * 添加一个图片
     * @param imagePojo 图片信息
     * @return true为成功，false为失败
     */
    boolean addImage(ImagePojo imagePojo);

    /**
     * 在User关联表中添加一个关联信息
     * @param userId 用户id
     * @param imageId 图片id
     * @return true为成功，false为失败
     */
    boolean addImageUserAss(Integer userId, Integer imageId);

    /**
     * 在Anime关联表中添加一个关联信息
     * @param animeId 动漫id
     * @param imageId 图片id
     * @return true为成功，false为失败
     */
    boolean addImageAnimeAss(Integer animeId, Integer imageId);

    /**
     * 在mmd关联表中添加一个关联信息
     * @param mmdId mmdId
     * @param imageId 图片id
     * @return true为成功，false为失败
     */
    boolean addImageMmdAss(Integer mmdId, Integer imageId);

    /**
     * 在game关联表中添加一个关联信息
     * @param gameId gameId
     * @param imageId 图片id
     * @return true为成功，false为失败
     */
    boolean addImageGameAss(Integer gameId, Integer imageId);

    /**
     * 修改一个图片信息
     * @param imageId 需要更新的图片id
     * @param newName 新图片名字
     * @return true为成功，false为失败
     */
    boolean updateImageName(Integer imageId, String newName);

    /**
     * 修改一个图片信息
     * @param imageId 需要更新的图片id
     * @param newUrl 新图片url
     * @return true为成功，false为失败
     */
    boolean updateImageUrl(Integer imageId, String newUrl);

    /**
     * 删除一个图片信息
     * @param imageId 图片id
     * @return true为成功，false为失败
     */
    boolean delImage(Integer imageId);

}
