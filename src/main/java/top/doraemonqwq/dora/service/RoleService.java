package top.doraemonqwq.dora.service;

import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.entity.security.Role;

import java.util.List;

/**
 * @author Doraemon
 * Role角色表的业务接口类
 */
@Service
public interface RoleService {

    /**
     * 查询全部数据
     * @return 将全部数据封装到list中返回
     */
    public List<Role> selectRole();

    /**
     * 通过id查询
     * @param roleId 需要查询的id
     * @return 返回查询结果的实体类
     */
    public Role selectRole(int roleId);

    /**
     * 通过name查询
     * @param roleName 需要查询的name
     * @return 返回查询结果的实体类
     */
    public Role selectRole(String roleName);

    /**
     * 添加一个数据
     * @param role 需要添加的数据的实体类
     * @return 返回添加成功的个数
     */
    public int insertRole(Role role);

    /**
     * 删除一个数据
     * @param roleId 需要删除的id
     * @return 返回删除成功的个数
     */
    public int deleteRole(int roleId);

    /**
     * 更新指定id的name(角色名)数据
     * @param roleId 需要更新的id
     * @param roleName 新的name数据
     * @return 返回更新成功的个数
     */
    public int updateNameRole(int roleId, String roleName);

    /**
     * 更新指定id的explanation(角色说明)数据
     * @param roleId 需要更新的id
     * @param roleExplanation 新的explanation数据
     * @return 返回更新成功的个数
     */
    public int updateExplanationRole(int roleId, String roleExplanation);



}
