package top.doraemonqwq.dora.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.doraemonqwq.dora.entity.pojo.ImagePojo;

import java.util.List;

/**
 * @author Doraemon
 * @date 2021-09-10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("UserDTO -- 返回用户数据的dto类")
public class UserDTO {
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("用户的角色")
    private List<String> roles;
    @ApiModelProperty("用户拥有的图片")
    private List<ImagePojo> images;
    @ApiModelProperty("最后登录时间")
    private String lastLoginTime;
}
