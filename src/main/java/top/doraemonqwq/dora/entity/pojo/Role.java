package top.doraemonqwq.dora.entity.pojo;

/**
 * @author Doraemon
 * role角色表的实体类
 */
public class Role {
    /**
     * roleId           角色id
     * roleName         角色名
     * roleExplanation  角色说明 表明该角色的权限范围是什么
     */
    private Integer roleId;
    private String roleName;
    private String roleExplanation;

    public Role() {}

    public Role(Integer roleId, String roleName, String roleExplanation) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleExplanation = roleExplanation;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleExplanation() {
        return roleExplanation;
    }

    public void setRoleExplanation(String roleExplanation) {
        this.roleExplanation = roleExplanation;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", roleExplanation='" + roleExplanation + '\'' +
                '}';
    }

}
