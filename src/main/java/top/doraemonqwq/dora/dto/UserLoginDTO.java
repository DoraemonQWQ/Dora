package top.doraemonqwq.dora.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Doraemon
 * @date 2021-09-10
 * 用于用户登录api的数据实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("UserLoginDTO -- 用户登录数据dto类")
public class UserLoginDTO {
    @ApiModelProperty("用户名登录，参数为username")
    public static String METHOD_USERNAME = "username";
    @ApiModelProperty("邮箱登录，参数为email")
    public static String METHOD_EMAIL = "email";

    @ApiModelProperty("用户名或邮箱")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    /**
     * 是否记住我 默认为false
     */
    @ApiModelProperty("是否记住我")
    private Boolean rememberMe = false;
    @ApiModelProperty("登录方式，默认为用户名方式登录")
    private String method = METHOD_USERNAME;

}
