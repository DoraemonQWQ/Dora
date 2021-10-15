package top.doraemonqwq.dora.service;

import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.entity.pojo.User;

import java.util.List;

/**
 * @author Doraemon
 * User的业务类接口
 */
@Service
public interface UserService {
    /**
     * 通过用户id查询用户信息
     * @param userId 需要查询的用户id
     * @return          User实体对象
     */
    User selectUser(Integer userId);

    /**
     * 通过用户名查询用户信息
     * @param username 需要查询的用户名
     * @return 返回用户信息
     */
    User selectUser(String username);

    /**
     * 通过邮箱查询用户信息
     * @param email 需要查询的邮箱
     * @return 返回用户信息
     */
    User selectUserByEmail(String email);

    /**
     * 查询全部用户信息
     * @return 用list包装的多个用户信息
     */
    List<User> selectUser();

    /**
     * 存储一个用户信息
     * @param user      需要添加的用户信息
     * @return          成功返回true，失败返回false
     */
    boolean insertUser(User user);

    /**
     * 删除一个用户信息
     * @param userId  需要删除的用户id
     * @return          成功返回true，失败返回false
     */
    boolean deleteUser(Integer userId);


    /**
     * 更新用户最后登录时间
     * @param userId   需要更新最后登录时间的用户id
     * @param lastLoginTime 新的最后登录时间
     * @return          成功返回true，失败返回false
     */
    boolean updateLoginTime(Integer userId, String lastLoginTime);

    /**
     * 更新用户的用户名
     * @param userId 需要更新的用户id
     * @param newUsername 新的用户名
     * @return 成功为true，失败为false
     */
    boolean updateUsername(Integer userId, String newUsername);

    /**
     * 更新用户最后登录时间
     * @param userId   需要更新密码的用户id
     * @param password 新的密码
     * @return          成功返回true，失败返回false
     */
    boolean updatePassword(Integer userId, String password);

    /**
     * 更新用户最后登录时间
     * @param userId   需要更新邮箱的用户id
     * @param email 新的邮箱
     * @return          成功返回true，失败返回false
     */
    boolean updateEmail(Integer userId, String email);

    /**
     * 查询token
     * @param userId 指定用户id
     * @return token
     */
    String getToken(Integer userId);


    /**
     * 更新token
     * @param userId 需要更新的用户id
     * @param token 新的token
     * @return TRUE为成功，FALSE为失败
     */
    boolean updateToken(Integer userId, String token);

    /**
     * 删除token
     * @param userId 需要删除的用户id
     * @return 成功返回true，失败返回false
     */
    boolean delToken(Integer userId);
}
