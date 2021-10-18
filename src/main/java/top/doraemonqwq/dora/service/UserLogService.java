package top.doraemonqwq.dora.service;

import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.entity.pojo.UserLogPojo;

import java.util.List;

/**
 * 用户操作日志服务接口
 * @author Doraemon
 */
@Service
public interface UserLogService {

    /**
     * 查询全部的用户日志
     * @return 全部的用户日志
     */
    List<UserLogPojo> selectUserLog();

    /**
     * 查询单个用户日志
     * @param id 日志id
     * @return 单个日志
     */
    UserLogPojo selectUserLogById(Integer id);

    /**
     * 添加一个用户日志
     * @param userLogPojo 用户日志的尸体列
     * @return 成功true 失败false
     */
    boolean addUserLog(UserLogPojo userLogPojo);



}
