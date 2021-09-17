package top.doraemonqwq.dora.dto;

import lombok.Data;

/**
 * @author Doraemon
 * @date 2021-09-11
 * 用户注册的数据传输类
 */
@Data
public class UserLoggingDTO {
    private String username;
    private String password;
    private String email;
}
