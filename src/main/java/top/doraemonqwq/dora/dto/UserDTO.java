package top.doraemonqwq.dora.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Doraemon
 * @date 2021-09-10
 */
@Data
public class UserDTO {
    private String username;
    private String email;
    private List<String> roles;
    private String lastLoginTime;
}
