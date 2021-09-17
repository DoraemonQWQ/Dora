package top.doraemonqwq.dora.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

/**
 * @author Doraemon
 * @date 2021-09-09
 * 全局跨域配置
 */
@Configuration
public class WebCorsConfiguration implements WebMvcConfigurer {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许携带cookie
        config.setAllowCredentials(true);
        // 放行所有的请求头信息
        config.addAllowedHeader("*");
        // 方向get和post的请求
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        // 开放所有的域
        config.addAllowedOrigin("*");

        // 添加映射l路径，/**代表全部路径都套用当前跨域访问权限的设置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);

        // 将source封装到CorsFilter并返回
        return new CorsFilter(source);
    }

}
