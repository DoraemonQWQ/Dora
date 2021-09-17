package top.doraemonqwq.dora.service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Doraemon
 * @date 2021-09-10
 * UserRole业务接口：
 * 主要业务是提供用户所拥有的角色的名称
 * 删除用户以及用户所拥有的角色的绑定
 */
@Service
public interface UserRoleService {

    /**
     * 提供用户名所拥有的角色的名称
     * @param username 指定的用户名
     * @return 返回拥有的角色名称
     */
    public List<String> listRoleNames(String username);

    /**
     * 将用户的角色添加到副表中
     * @param username 指定的用户名
     * @param roles 需要添加的角色
     * @return true为成功，false为失败
     */
    public boolean insertUserRoleAss(String username, List<String> roles);

}
