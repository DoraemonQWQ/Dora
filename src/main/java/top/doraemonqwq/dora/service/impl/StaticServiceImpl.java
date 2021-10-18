package top.doraemonqwq.dora.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.entity.pojo.ImagePojo;
import top.doraemonqwq.dora.entity.pojo.VideoPojo;
import top.doraemonqwq.dora.service.ImagesService;
import top.doraemonqwq.dora.service.StaticService;
import top.doraemonqwq.dora.service.VideosService;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Doraemon
 * StaticService的实现类
 */
@Service("StaticServiceImpl")
@Slf4j
public class StaticServiceImpl implements StaticService {

    public static final String MAP_IMAGE_NAME = "image";
    public static final String MAP_VIDEO_NAME = "video";
    public static final String MAP_TYPE_NAME = "type";

    @Autowired
    @Qualifier("VideosServiceImpl")
    VideosService videosService;

    @Autowired
    @Qualifier("ImagesServiceImpl")
    ImagesService imagesService;


    /**
     * 获取图片的字节流
     *
     * @param imageId 图片id
     * @return 返回
     */
    @Override
    public Map<String,Object> getImage(Integer imageId) {
        Map<String, Object> map = new HashMap<>(2);
        ImagePojo imagePojo = imagesService.selectImageById(imageId);
        try {

            Path path = Paths.get(imagePojo.getUrl());
            // 判断文件存不存在
            if (Files.exists(path)) {
                // 获取文件类型
                String type = Files.probeContentType(path);
                if (StrUtil.isEmpty(type)) {
                    // 如果读取的文件类型为null，那么返回空map
                    return map;
                }
                map.put(MAP_TYPE_NAME, type);
                map.put(MAP_IMAGE_NAME , ImageIO.read(new FileInputStream(path.toFile())));
            }
            return map;
        } catch (IOException e) {
            log.error("图片读取异常", e);
        }
        return map;
    }

    /**
     * 获取视频的path对象
     *
     * @param videoId 视频id
     */
    @Override
    public Map<String, Object> getVideo(Integer videoId) {
        Map<String, Object> map = new HashMap<>(2);
        VideoPojo videoPojo = videosService.selectVideoById(videoId);

        try {
            Path path = Paths.get(videoPojo.getUrl());
            // 判断文件存不存在
            if (Files.exists(path)) {
                // 获取文件类型
                String type = Files.probeContentType(path);
                if (StrUtil.isEmpty(type)) {
                    // 如果文件类型为null，返回空map
                    return map;
                }
                map.put(MAP_TYPE_NAME, type);
                map.put(MAP_VIDEO_NAME, path);
            }
            return map;
        } catch (IOException e) {
            log.error("读取视频异常",e);
        }
        return map;
    }
}
