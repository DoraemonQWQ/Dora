package top.doraemonqwq.dora.security.customize;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import top.doraemonqwq.dora.security.pojo.ResponseResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Doraemon
 * @date 2021-09-11
 * 未登录的自定义响应
 */
public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");

        // 创建错误响应码401
        int statusCode = HttpStatus.HTTP_UNAUTHORIZED;
        // 将响应码写入response中
        response.setStatus(statusCode);
        // 获取当前的url
        String path = request.getRequestURI();
        // 错误提示
        String msg = StrUtil.format("匿名用户访问 {} 失败；原因：未登录。响应 {} 代码", path, statusCode);
        // 将错误提示写入response中，并返回给前端
        response.getWriter().print(ResponseResult.create().error(statusCode, msg, path));
    }
}
