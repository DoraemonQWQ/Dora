package top.doraemonqwq.dora.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 资源的路径
 */
@Component
public final class ResourceUrlConstants {
    /**
     * 视频资源请求接口
     */
    public static final String VIDEO_API = "/api/resource/video/**";

    /**
     * 图片资源请求接口
     */
    public static final String IMAGE_API = "/api/resource/image/**";

    /**
     * 视频资源路径
     */
    public static String VIDEO_PATH;

    /**
     * 图片资源路径
     */
    public static String IMAGE_PATH;


    @Value("${path.video}")
    public void setVideoPath(String videoPath) {
        VIDEO_PATH = videoPath;
    }

    @Value("${path.image}")
    public void setImagePath(String imagePath) {
        IMAGE_PATH = imagePath;
    }

}
