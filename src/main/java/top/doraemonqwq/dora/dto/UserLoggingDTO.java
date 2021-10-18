package top.doraemonqwq.dora.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Doraemon
 * @date 2021-09-11
 * 用户注册的数据传输类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("UserLoggingDTO -- 用户注册数据dto类")
public class UserLoggingDTO {
    @ApiModelProperty("注册的用户名")
    private String username;
    @ApiModelProperty("注册的密码")
    private String password;
    @ApiModelProperty("注册的邮箱")
    private String email;
}
