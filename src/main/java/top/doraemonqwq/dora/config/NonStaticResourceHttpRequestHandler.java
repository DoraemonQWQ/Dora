package top.doraemonqwq.dora.config;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Doraemon
 * @date 2021-09-19
 * 加载视频流
 */
@Component
public class NonStaticResourceHttpRequestHandler extends ResourceHttpRequestHandler {

    public String filepath = "filepath";

    @Override
    protected Resource getResource(HttpServletRequest request) {

        final Path filePath = (Path) request.getAttribute(filepath);

        return new FileSystemResource(filePath);

    }
}
