package top.doraemonqwq.dora.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.doraemonqwq.dora.entity.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @author Doraemon
 * user表的mapper类
 */
@Mapper
@Repository
public interface UserMapper {
    /**
     * 通过用户名的方式查询用户信息
     * @param username  需要查询用户信息的用户名
     * @return          User实体对象
     */
    User selectByUsernameUser(@Param("username") String username);

    /**
     * 通过用户id的方式查询用户信息
     * @param userId 查询的用户ID
     * @return user实体对象
     */
    User selectByUserIdUser(@Param("userId") Integer userId);

    /**
     * 通过邮箱查询用户信息
     * @param email 需要查询的邮箱
     * @return 返回用户信息
     */
    User selectUserByEmail(@Param("email") String email);

    /**
     * 查询全部用户信息
     * @return          User实体对象
     */
    List<User> selectAllUser();

    /**
     * 存储一个用户信息
     * @param user      需要添加的用户信息
     * @return          返回0或1，0代表失败，1代表成功
     */
    int insertUser(User user);

    /**
     * 删除一个用户信息
     * @param userId  需要删除的用户id
     * @return          返回0或1，0代表失败，1代表成功
     */
    int deleteUser(@Param("userId") Integer userId);

    /**
     * 更新一个用户信息
     * @param map   新的用户信息
     * @return          返回0或1，0代表失败，1代表成功
     */
    int updateUser(Map<String, Object> map);

    /**
     * 查询指定用户的token
     * @param userId 需要查询的用户名
     * @return token
     */
    String selectToken(@Param("userId") Integer userId);


}
