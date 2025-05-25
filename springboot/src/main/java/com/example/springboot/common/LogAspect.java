package com.example.springboot.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Employee;
import com.example.springboot.entity.Log;
import com.example.springboot.service.LogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Objects;

@Component
@Aspect
public class LogAspect {

    @Resource
    private LogService logService;

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtils.class);

    @Around("@annotation(autoLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, AutoLog autoLog) throws Throwable {
        String type = autoLog.value();
        String time = DateUtil.now();

        String username = "";
        String name = "";

        Account user = JwtTokenUtils.getCurrentAccount();
        if (ObjectUtil.isNotNull(user)) {
            System.out.print("打印登錄信息"+user);
            username = user.getUsername();
            name = user.getName();
        }

        // 操作人 IP
        String ip = getClientIpAddress();

        // 執行原方法
//        Object resultObj = joinPoint.proceed();
        Result result = (Result) joinPoint.proceed();

        if (result == null) {
            // 這邊可以根據需求設定預設值、記錄日誌，或直接回傳一個錯誤結果
            return null; // 或你習慣的錯誤處理方式
        }
        // 如果方法回傳 Account 資料，記錄 name 與 username
        Object data = result.getData();
//        if (data instanceof Account) {
//            Account account = (Account) data;
//            name = account.getName();
//            username = account.getUsername();
//        }
        if (Objects.equals(name, "")) {
            Account account = (Account) data;
            name = account.getName();
            username = account.getUsername();
        }

        Log log = new Log(null, time, name, username, type, ip);
        logService.add(log);

        return result;
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String ip = request.getHeader("X-Forwarded-For");
            if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            // 对于多级代理的情况，取第一个非unknown的IP
            if (StrUtil.isNotBlank(ip) && ip.contains(",")) {
                ip = Arrays.stream(ip.split(","))
                        .map(String::trim)
                        .filter(i -> !"unknown".equalsIgnoreCase(i))
                        .findFirst()
                        .orElse(ip.split(",")[0].trim());
            }
            return ip;
        } catch (Exception e) {
            log.warn("获取客户端IP地址失败", e);
            return "unknown";
        }
    }
}

