package top.doraemonqwq.dora.dto;

import lombok.Data;

/**
 * @author Doraemon
 * @date 2021-09-10
 * 用于用户登录api的数据实体类
 */
@Data
public class UserLoginDTO {

    public static String METHOD_USERNAME = "username";
    public static String METHOD_EMAIL = "email";

    private String username;
    private String password;
    /**
     * 是否记住我 默认为false
     */
    private Boolean rememberMe = false;
    private String method = METHOD_USERNAME;

}
