package top.doraemonqwq.dora.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Doraemon
 * @date 2021-09-10
 */
@Mapper
@Repository
public interface UserRoleMapper {

    /**
     * 通过用户id，查询用户的角色名
     * @param username 需要查询的用户名
     * @return 返回全部用户所属的角色名
     */
    public List<String> selectRoleNameByUsername(@Param("username") String username);


    /**
     * 删除指定Userid的User表和Role表的副表的数据
     * @param userId 指定userId
     */
    public int deleteUserRoleAss(@Param("userId") Integer userId);

    /**
     * 添加指定的UserId和RoleId到副表中
     * @param userId 需要添加的userId
     * @param roleId 需要添加的roleId
     */
    public int insertUserRoleAss(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

}
