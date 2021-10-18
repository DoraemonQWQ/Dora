package top.doraemonqwq.dora.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Doraemon
 * @date 2021-09-17
 * 修改用户数据的传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("UserAlterDTO -- 修改用户数据的dto类")
public class UserAlterDTO {
    /**
     * username: 用户名
     * email: 邮箱
     * sign: 个人签名
     */
    @ApiModelProperty("用户的个人简介")
    private String sign;
}
