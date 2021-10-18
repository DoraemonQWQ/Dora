package top.doraemonqwq.dora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author Doraemon
 * 项目的启动类
 */
@SpringBootApplication
@EnableOpenApi
public class DoraApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoraApplication.class, args);
    }

}
