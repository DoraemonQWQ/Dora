package top.doraemonqwq.dora.aop.Aspect;

import cn.hutool.json.JSONUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import top.doraemonqwq.dora.aop.UserLog;
import top.doraemonqwq.dora.entity.pojo.UserLogPojo;
import top.doraemonqwq.dora.service.UserLogService;
import top.doraemonqwq.dora.utils.HttpContextUtil;
import top.doraemonqwq.dora.utils.IpUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 用户操作日志的aop实现
 * @author Doraemon
 */
@Aspect
@Component
public class UserLogAspect {

    @Autowired
    @Qualifier("UserLogServiceImpl")
    UserLogService userLogService;

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation(top.doraemonqwq.dora.aop.UserLog)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        //保存日志
        UserLogPojo userLogPojo = new UserLogPojo();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();

        //获取操作
        UserLog userLog = method.getAnnotation(UserLog.class);
        if (userLog != null) {
            String value = userLog.value();
            userLogPojo.setOperation(value);//保存获取的操作
        }

        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        userLogPojo.setMethod(className + "." + methodName);

        //请求的参数
        Object[] args = joinPoint.getArgs();
        //将参数所在的数组转换成json
        String params = JSONUtil.parse(args).toString();
        userLogPojo.setParams(params);

        userLogPojo.setCreateDate(new Date());

        // 获取上下文
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 判断上下文是否为空
        if (authentication == null) {
            // 为空，id为null
            userLogPojo.setUserId(null);
        } else {
            Object principal = authentication.getPrincipal();
            // 如果从上下文获得的数据为anonymousUser，那么就代表没有用户登录
            if ("anonymousUser".equals(principal.toString())) {
                // 存入0代表用户未登录
                userLogPojo.setUserId(0);
            } else {
                //获取用户id
                Integer userId = Integer.valueOf(principal.toString());
                userLogPojo.setUserId(userId);
            }

        }


        //获取用户ip地址
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        userLogPojo.setIp(IpUtil.getIpAddr(request));

        //调用service保存SysLog实体类到数据库
        userLogService.addUserLog(userLogPojo);
    }

}
