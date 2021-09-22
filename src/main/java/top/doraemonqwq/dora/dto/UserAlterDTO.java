package top.doraemonqwq.dora.dto;

import lombok.Data;

/**
 * @author Doraemon
 * @date 2021-09-17
 * 修改用户数据的传输对象
 */
@Data
public class UserAlterDTO {
    /**
     * username: 用户名
     * email: 邮箱
     * sign: 个人签名
     */
    private String username;
    private String email;
    private String sign;
}
