package top.doraemonqwq.dora.security.pojo;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import top.doraemonqwq.dora.dto.UserDTO;
import top.doraemonqwq.dora.entity.pojo.User;

import java.util.List;

/**
 * @author Doraemon
 * @date 2021-09-10
 * 包含token的UserLoginDTO实体类
 */
public class JwtUser {

    private UserDTO userDTO;
    private String token;
    /**
     * 用于判断JwtUser是否为空
     */
    private boolean isNull = true;
    /**
     * 请求头
     */
    private String header;

    /**
     * 错误信息 默认为空
     */
    private String error;

    public static JwtUser create() {
        return new JwtUser();
    }

    public JwtUser setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
        this.isNull = false;
        return this;
    }

    public UserDTO getUserDTO() {
        return this.userDTO;
    }

    public JwtUser setToken(String token) {
        this.isNull = false;
        this.token = token;
        return this;
    }

    public String getToken() {
        return this.token;
    }

    public JwtUser setHeader(String header) {
        this.header = header;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public boolean isNull() {
        return this.isNull;
    }

    public void addTokenBearer(String bearer) {
        StringBuilder bufferToken = new StringBuilder(this.token);
        this.token = bufferToken.insert(0, bearer).toString();
    }

    public JwtUser userToUserDTO(User user, List<String> roles) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setLastLoginTime(user.getLastLoginTime());
        userDTO.setRoles(roles);
        this.userDTO = userDTO;
        return this;
    }

    public JwtUser setError(String error) {
        this.error = error;
        return this;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return JSONUtil.parse(this).toString();
    }

    public JSON toJSON() {
        return JSONUtil.parse(this);
    }
}
