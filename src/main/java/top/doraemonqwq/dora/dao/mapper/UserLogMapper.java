package top.doraemonqwq.dora.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.doraemonqwq.dora.entity.pojo.UserLogPojo;

import java.util.List;

/**
 * @author Doraemon
 * 用户操作日志表的Mapper接口
 */
@Mapper
@Repository
public interface UserLogMapper {

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
    UserLogPojo selectUserLogById(@Param("id") Integer id);

    /**
     * 添加一个用户日志
     * @param userLogPojo 用户日志的尸体列
     */
    int addUserLog(UserLogPojo userLogPojo);

}
