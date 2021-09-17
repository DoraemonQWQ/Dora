package top.doraemonqwq.dora.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import top.doraemonqwq.dora.constant.RoleConstants;
import top.doraemonqwq.dora.constant.SecurityConstants;
import top.doraemonqwq.dora.entity.security.Role;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Doraemon
 * @date 2021-09-09
 * Jwt工具类
 */
public class JwtUtil {

    // 生成token的key
    private static final byte[] tokenKey = DatatypeConverter.parseBase64Binary(SecurityConstants.JWT_SECRET_KEY);

    /**
     * 根据用户名和用户角色生成token
     * @param userId 用户id
     * @param roles 用户所对应的角色名
     * @param isRemember 是否记住
     * @return 返回token
     */
    public static String generateToken(Integer userId, List<String> roles, boolean isRemember) {
        // 得到token的过期时间
        long expiration = isRemember ? SecurityConstants.EXPIRATION_REMEMBER_TIME : SecurityConstants.EXPIRATION_TIME;
        // 将所有的载荷信息，存入token中
        String token = JWT.create()
                .setHeader("JWT", SecurityConstants.TOKEN_TYPE)
                .setPayload("userId", userId)
                .setPayload("roles", roles)
                .setPayload("expiration", expiration)
                .setKey(tokenKey)
                .sign();
        return token;
    }

    /**
     * 验证token的有效性
     * @param token 需要验证的token
     * @return true为有效，false为无效
     */
    public static boolean validateToken(String token) {
        return JWTUtil.verify(token, tokenKey);

    }


    /**
     * 解析token中存储的用户信息
     * @param token 需要解析的token
     * @return 返回用户信息
     */
    public static Authentication getAuthentication(String token) {
        // 解析token
        JWT jwt = JWTUtil.parseToken(token);
        // 得到用户id
        Integer userId = (Integer) jwt.getPayload("userId");
        // 获得角色名
        List<String> roles = (List<String>) jwt.getPayload("roles");
        // 将用户名封装到另一个list集合中
        List<SimpleGrantedAuthority> authorities =
                Objects.isNull(roles) ? Collections.singletonList(new SimpleGrantedAuthority(RoleConstants.ROLE_USER_DEFAULT)) :
                        roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());


        return new UsernamePasswordAuthenticationToken(userId, token, authorities);

    }



}
