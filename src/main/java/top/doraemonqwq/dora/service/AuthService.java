package top.doraemonqwq.dora.service;

import io.jsonwebtoken.Jwt;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.dto.UserLoggingDTO;
import top.doraemonqwq.dora.security.pojo.JwtUser;
import top.doraemonqwq.dora.dto.UserLoginDTO;

/**
 * @author Doraemon
 * @date 2021-09-10
 * 用户授权业务类
 */
@Service
public interface AuthService {

    /**
     * 登录业务，生成该用的token后封装到JwtUser返回
     * @param userLoginDTO 用户登录的数据
     * @return JwtUser
     */
    public JwtUser authLogin(UserLoginDTO userLoginDTO);

    /**
     * 注册业务，通过传入用户的用户名和密码和邮箱，生成token，然后封装成JwtUser返回
     * @param userLoggingDTO 用户注册的数据
     * @return JwtUser
     */
    public JwtUser authLogging(UserLoggingDTO userLoggingDTO);

    /**
     * 注销业务，删除security中存储的上下文
     * @param userId 需要清除token的id
     */
    public void authLogout(Integer userId);

}
