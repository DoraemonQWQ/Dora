package top.doraemonqwq.dora.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import top.doraemonqwq.dora.constant.ResourceUrlConstants;
import top.doraemonqwq.dora.constant.SecurityConstants;
import top.doraemonqwq.dora.filter.JwtAuthorizationFilter;
import top.doraemonqwq.dora.security.customize.CustomizeAccessDeniedHandler;
import top.doraemonqwq.dora.security.customize.CustomizeAuthenticationEntryPoint;


/**
 * @author Doraemon
 * @date 2021-09-09
 * security配置类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    CorsFilter corsFilter;

    @Autowired
    SecurityProblemSupport securityProblemSupport;

    /**
     * 将密码加密方案添加到ioc容器中
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 此方法内的资源路径请求，都会跳过security认证
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                // 忽略所有options请求
                .antMatchers(HttpMethod.OPTIONS, "/**")
                // 忽略所有的静态资源请求
                .antMatchers("/app/**/*.{js,html}")
                // 忽略所有api文档请求
                .antMatchers("/v2/api.docs/**")
                // 忽略所有国际化请求
                .antMatchers("/i18n/**")
                // 忽略test请求
                .antMatchers("/test/**")
                .antMatchers("/h2")
                .antMatchers("/constant/**")
                // 忽略所有swagger相关请求路径
                .antMatchers("/swagger-ui/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/v3/**");

    }

    private JwtConfigurer securityConfigurationAdapter() throws Exception {
        return new JwtConfigurer(new JwtAuthorizationFilter(authenticationManager()));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                // 当无权限访问时，返回401响应
                .authenticationEntryPoint(new CustomizeAuthenticationEntryPoint())
                // 当权限过低访问时，返回403响应
                .accessDeniedHandler(new CustomizeAccessDeniedHandler())
            .and()
                // 禁用 CSRF
                .csrf().disable()
                .headers().frameOptions().disable()
            .and()
                .authorizeRequests()
                // 指定路径下的所有请求需要验证
                .antMatchers("/").permitAll()
                // 放行登录路径和注册路径
                .antMatchers(HttpMethod.POST, SecurityConstants.AUTH_LOGIN_URL).permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.AUTH_LOGGING_URL).permitAll()
                // 放行公开页面的数据请求
                .antMatchers(HttpMethod.POST, "/api/public-data/**").permitAll()
                // 放行视频请求接口
                .antMatchers(HttpMethod.GET, ResourceUrlConstants.VIDEO_API).permitAll()
                // 放行图片请求接口
                .antMatchers(HttpMethod.GET, ResourceUrlConstants.IMAGE_API).permitAll()
                // 放行测试接口
                .antMatchers(HttpMethod.GET, "/api/test/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/test/**").permitAll()
                // 其余的请求路径都需要验证
                .anyRequest().authenticated()
            .and()
                // 不需要session(不创建会话)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                // 导入jwt拦截器
                .apply(securityConfigurationAdapter());

    }
}
