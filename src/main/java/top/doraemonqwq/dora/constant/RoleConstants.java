package top.doraemonqwq.dora.constant;

/**
 * @author Doraemon
 * @date 2021-09-10
 * 角色名常量类 包含所有角色名
 */
public class RoleConstants {

    public RoleConstants() {
        throw new IllegalStateException("Cannot create instance of static constant class");
    }

    /**
     * 管理员角色的名称
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    /**
     * 表站角色的名称
     */
    public static final String ROLE_USER_G = "ROLE_USER_G";
    /**
     * 里站角色的名称
     */
    public static final String ROLE_USER_R18 = "ROLE_USER_R18";
    /**
     * 默认角色的名称
     */
    public static final String ROLE_USER_DEFAULT = "ROLE_USER_G";
}
