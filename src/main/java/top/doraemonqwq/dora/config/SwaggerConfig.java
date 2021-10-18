package top.doraemonqwq.dora.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * swagger配置类
 * @author Doraemon
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig implements WebMvcConfigurer {

    private final Contact contact = new Contact("小叮当", "https://github.com/DoraemonQWQ", "3028029456@qq.com");

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                /*
                    作者信息
                 */
                .contact(contact)
                /*
                    API页面的标题
                 */
                .title("Dora Api Doc")
                /*
                    API页面的接口描述
                 */
                .description("Dora的后端请求api文档")
                /*
                    组织的url地址
                 */
                .termsOfServiceUrl(null)
                /*
                    API对应的接口的版本号
                 */
                .version("v1.0")
                /*
                    开源组织名称
                 */
                .license(null)
                /*
                    开源组织的url地址
                 */
                .licenseUrl(null)
                .build();
    }

    @Bean
    public Docket docket(Environment environment) {
        Profiles profiles = Profiles.of("dev", "test");
        boolean flag = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.OAS_30)
                /*
                    配置Api页面的标题以及描述
                 */
                .apiInfo(apiInfo())
                .enable(flag)
                .select()
                /*
                    RequestHandlerSelectors.any() 扫描全部的接口
                    RequestHandlerSelectors.none() 不扫描
                    RequestHandlerSelectors.basePackage() 指定扫描的包
                    RequestHandlerSelectors.withClassAnnotation() 指定扫描类上的注解
                    RequestHandlerSelectors.withMethodAnnotation() 指定扫描方法上的注解
                 */
                .apis(RequestHandlerSelectors.any())
                /*
                    PathSelectors.any() 拦截所有请求
                    PathSelectors.none() 不拦截请求
                    PathSelectors.ant() 拦截指定的请求
                    PathSelectors.regex() 不拦截指定的请求
                 */
                .paths(PathSelectors.any())
                .build();
    }

}
