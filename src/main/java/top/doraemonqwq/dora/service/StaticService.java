package top.doraemonqwq.dora.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Doraemon
 * 静态资源的业务接口
 */
@Service
public interface StaticService {

    /**
     * 获取图片的字节流
     * @param imageId 图片id
     * @return 返回
     */
    Map<String, Object> getImage(Integer imageId);

    /**
     * 获取视频的path对象
     * @param videoId 视频id
     */
    Map<String, Object> getVideo(Integer videoId);

}
