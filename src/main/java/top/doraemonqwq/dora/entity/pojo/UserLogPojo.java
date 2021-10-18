package top.doraemonqwq.dora.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户操作日志表的实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogPojo {
    private Integer id;
    private Integer userId;
    private String operation;
    private String method;
    private String params;
    private String ip;
    private Date createDate;

    public UserLogPojo(Integer userId, String operation, String method, String params, String ip, Date createDate) {
        this.userId = userId;
        this.operation = operation;
        this.method = method;
        this.params = params;
        this.ip = ip;
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (userId == 0) {
            return format.format(createDate) + " " +
                    "[IP地址:" + ip + "] " +
                    "[用户id:未登录账号访问] " +
                    "[用户操作:" +  operation + "] " +
                    "[调用的方法:" + method + "] " +
                    "[参数:" + params + "]";
        } else {
            return format.format(createDate) + " " +
                    "[IP地址:" + ip + "] " +
                    "[用户id:" + userId + "] " +
                    "[用户操作:" +  operation + "] " +
                    "[调用的方法:" + method + "] " +
                    "[参数:" + params + "]";
        }

    }
}
