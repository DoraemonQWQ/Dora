package top.doraemonqwq.dora.aop;

import java.lang.annotation.*;

/**
 * 用户操作日志的注解
 * @author Doraemon
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented // 生成文档
public @interface UserLog {
    String value() default "";
}
