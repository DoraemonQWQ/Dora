package top.doraemonqwq.dora.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.doraemonqwq.dora.config.NonStaticResourceHttpRequestHandler;
import top.doraemonqwq.dora.service.StaticService;
import top.doraemonqwq.dora.service.impl.StaticServiceImpl;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author Doraemon
 * 资源请求控制器
 */
@RestController
@RequestMapping("/api/resource/")
@Api("静态资源Api")
@Slf4j
public class ResourceController {

    @Autowired
    @Qualifier("StaticServiceImpl")
    StaticService staticService;

    @Autowired
    NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    @GetMapping("/image/{id}")
    @ApiOperation("请求图片资源api -- /api/resource/image/{id}")
    @ApiParam("图片的id -- id")
    public void getImage(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(id);
        Map<String, Object> imageMap = staticService.getImage(id);
        String type = (String) imageMap.get(StaticServiceImpl.MAP_TYPE_NAME);
        BufferedImage image = (BufferedImage) imageMap.get(StaticServiceImpl.MAP_IMAGE_NAME);
        try (OutputStream os = response.getOutputStream()) {
            // 设置文件类型
            response.setContentType(type);
            if (image != null) {
                // 读取image对象，写入到resp中，将type用"/"切分后取得文件的类型
                ImageIO.write(image, type.split("/")[1],os);
            }

        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

            log.error("资源传输错误",e);
        }

    }

    @GetMapping("/video/{id}")
    @ApiOperation("请求视频资源api -- /api/resource/video/{id}")
    @ApiParam("视频的id -- id")
    public void getVideo(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(id);
        Map<String, Object> videoMap = staticService.getVideo(id);
        String type = (String) videoMap.get(StaticServiceImpl.MAP_TYPE_NAME);
        Path path = (Path) videoMap.get(StaticServiceImpl.MAP_VIDEO_NAME);

        try {
            response.setContentType(type);

            // 添加请求头
            request.setAttribute(nonStaticResourceHttpRequestHandler.filepath, path);
            // 利用 nonStaticResourceHttpRequestHandler.handleRequest() 实现返回视频流
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } catch (ServletException | IOException e) {
            log.error("传输文件出错",e);
        }

    }

}
