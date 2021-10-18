package top.doraemonqwq.dora.entity.pojo;

/**
 * @author Doraemon
 * User表的实体类
 */
public class User {
    /**
     * userId           用户id
     * username         用户名
     * password         密码
     * email            注册邮箱
     * creationDate     创建时间
     * lastLoginTime    最后登录时间
     */
    private Integer userId;
    private String username;
    private String password;
    private String email;
    private String creationDate;
    private String lastLoginTime;
    private String introduction;

    public User(String username, String password, String email, String creationDate, String lastLoginTime) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.creationDate = creationDate;
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                '}';
    }
}
