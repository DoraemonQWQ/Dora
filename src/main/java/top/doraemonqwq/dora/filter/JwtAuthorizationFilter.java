package top.doraemonqwq.dora.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import top.doraemonqwq.dora.constant.SecurityConstants;
import top.doraemonqwq.dora.service.UserService;
import top.doraemonqwq.dora.service.impl.UserServiceImpl;
import top.doraemonqwq.dora.utils.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Doraemon
 * @date 2021-09-09
 * 用户请求授权过滤器
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    UserService userService;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
         // 从http中获取token
        String token = this.getToken(request);

        // 装配userService
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        userService = factory.getBean(UserServiceImpl.class);

        // 验证token是否有效
        if (StringUtils.hasText(token) && JwtUtil.validateToken(token)) {
            // 从token中取出用户id，通过用户id从redis中得到token，然后进行token的判断
            JWT jwt = JWTUtil.parseToken(token);
            Integer userId = (Integer) jwt.getPayload("userId");
            String userToken = userService.getToken(userId);

            // 判断token是否相同
            if (userToken.equals(token)) {

                // 获取用户信息
                Authentication authentication = JwtUtil.getAuthentication(token);
                // 将用户信息存储到 security 上下文中
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }


        }

        // 放行请求
        chain.doFilter(request,response);
    }

    /**
     * 从http中获取token
     * @param request 目标http
     * @return 将获取到的token返回
     */
    private String getToken(HttpServletRequest request) {
        // 从http中获取指定的请求头
        String header = request.getHeader(SecurityConstants.TOKEN_HEADER);
        // 判断请求头内有没有Bearer标注，如果没有，就返回null
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return null;
        }
        // 去除token前端，取出token
        return header.replace(SecurityConstants.TOKEN_PREFIX, "");
    }
}
