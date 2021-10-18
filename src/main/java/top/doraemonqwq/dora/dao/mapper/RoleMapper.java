package top.doraemonqwq.dora.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.doraemonqwq.dora.entity.pojo.Role;

import java.util.List;
import java.util.Map;

/**
 * @author hequanshawu
 * Role角色表的mapper接口
 */
@Mapper
@Repository
public interface RoleMapper {

    /**
     * 查询全部数据
     * @return 返回查询结果，用list包装
     */
    List<Role> selectAllRole();

    /**
     * 查询单个数据 roleId和name 二选一查询
     * @param roleId 需要查询的角色id
     * @param name 需要查询的角色名
     * @return 返回查询的数据，用实体类包装
     */
    Role selectOneRole(@Param("roleId") int roleId, @Param("roleName") String name);

    /**
     * 添加一个数据
     * @param role 需要添加的数据的实体类
     * @return 返回0或1，0代表失败1代表成功
     */
    int insertRole(Role role);

    /**
     * 删除一个数据
     * @param roleId 需要删除的角色id
     * @return 返回0或1，0代表失败1代表成功
     */
    int deleteRole(@Param("roleId") int roleId);

    /**
     * 更新一条数据 数据内容用map包装，map中必须包含roleId键(int)，其余可选键为roleName(String), roleExplanation(String)
     * @param map 需要更新的map对象
     * @return 返回0或1，0代表失败1代表成功
     */
    int updateRole(Map<String, Object> map);


}
