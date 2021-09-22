package top.doraemonqwq.dora.security.customize;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.util.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import top.doraemonqwq.dora.constant.SecurityConstants;
import top.doraemonqwq.dora.security.pojo.ResponseResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Doraemon
 * @date 2021-09-11
 * 权限不足的自定义响应
 */
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");

        // 创建错误响应码403
        int statusCode = HttpStatus.HTTP_FORBIDDEN;
//        String header = request.getHeader(SecurityConstants.TOKEN_HEADER);
        // 获取访问当前url的用户名
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 获取当前的url
        String path = request.getRequestURI();
        // 设置错误提示，将这个错误提示保存到日志里
        String msg = StrUtil.format("{} 用户访问 {} 失败；原因：权限不足。响应 {} 代码", username, path, statusCode);
        // 将错误信息返回给前端
        response.getWriter().print(ResponseResult.create().error(statusCode,msg,path));
    }
}
