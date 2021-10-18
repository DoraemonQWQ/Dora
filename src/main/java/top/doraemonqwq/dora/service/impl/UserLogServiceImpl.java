package top.doraemonqwq.dora.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.doraemonqwq.dora.dao.mapper.UserLogMapper;
import top.doraemonqwq.dora.entity.pojo.UserLogPojo;
import top.doraemonqwq.dora.service.UserLogService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 用户操作日志服务实现类
 * @author Doraemon
 */
@Service("UserLogServiceImpl")
@Slf4j
public class UserLogServiceImpl implements UserLogService {

    @Value("${log.path}")
    private String logPath;

    @Autowired
    UserLogMapper mapper;

    /**
     * 查询全部的用户日志
     *
     * @return 全部的用户日志
     */
    @Override
    public List<UserLogPojo> selectUserLog() {
        return mapper.selectUserLog();
    }

    /**
     * 查询单个用户日志
     *
     * @param id 日志id
     * @return 单个日志
     */
    @Override
    public UserLogPojo selectUserLogById(Integer id) {
        return mapper.selectUserLogById(id);
    }

    /**
     * 添加一个用户日志
     *
     * @param userLogPojo 用户日志的尸体列
     * @return 成功true 失败false
     */
    @Override
    public boolean addUserLog(UserLogPojo userLogPojo) {
        try {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss");

            if (!logPath.endsWith("/")) {
                logPath += "/";
            }

            logPath = logPath + "user_log_" + LocalDate.now().format(dateFormatter);
            File pathFile = new File(logPath);
            if (!pathFile.isDirectory()) {
                // 文件夹不存在，创建一个新的文件夹
                pathFile.mkdir();
            }
            // 在目标文件夹下写入log文件
            File logFile = new File(logPath + "operate-log.log");
            if (!logFile.isFile()) {
                // 当前日志不存在，创建一个新的日志
                if (logFile.createNewFile()) {
                    log.info("创建日志文件成功");
                } else {
                    log.error("创建日志文件失败");
                }
            } else if(logFile.length() >= 1000 * 1000 * 10) {
                // 如果存在，并且文件大小已经又100m了，那么重命名并创建一个新的文件
                if (logFile.renameTo(new File(logPath + "operate-log-" + LocalTime.now().format(timeFormatter) + ".log"))) {
                    log.info("用户操作日志文件重命名成功");
                    // 重新创建一个文件
                    if (logFile.createNewFile()) {
                        log.info("再次创建日志文件成功");
                    } else {
                        log.error("再次创建日志文件失败");
                    }

                } else {
                    log.error("用户操作日志文件重命名失败");
                }
            }
            try ( FileWriter fileWriter = new FileWriter(logFile, true);
                  PrintWriter printWriter = new PrintWriter(fileWriter)) {
                printWriter.println(userLogPojo.toString());
                printWriter.flush();
                fileWriter.flush();
            }


        } catch (IOException e) {
            log.error("创建用户操作日志文件失败",e);
        }

        return mapper.addUserLog(userLogPojo) > 0;
    }
}
