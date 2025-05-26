package com.example.springboot.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Employee;
import com.example.springboot.entity.Log;
import com.example.springboot.service.LogService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.*;

@Component
@Aspect
public class LogAspect {
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        String json = serializeParameters(joinPoint.getArgs());
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
        Account account = (Account) data;
        if (Objects.equals(name, "")) {
            name = account.getName();
            username = account.getUsername();
        }
        if (Objects.equals(name, "")) {
            account.setName("System");
            account.setUsername("System");
            name = account.getName();
            username = account.getUsername();
        }
        Log log = new Log(null, time, name, username, type, json, ip);
        logService.add(log);

        return result;
    }

    private String serializeParameters(Object[] args) {
        ObjectMapper mapper = new ObjectMapper();
        // 避免序列化 null 值
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 要排除的欄位名稱（敏感或不需要記錄的）
        Set<String> excludedFields = Set.of("password", "token", "newPassword");

        List<Map<String, Object>> filteredList = new ArrayList<>();

        for (Object arg : args) {
            if (arg == null) {
                continue;
            }

            // 處理非基本類型（如自定義物件）
            if (!(arg instanceof String) && !(arg instanceof Number) && !(arg instanceof Boolean)) {
                try {
                    // 將物件轉換成 Map，方便欄位過濾
                    Map<String, Object> map = mapper.convertValue(arg, new TypeReference<Map<String, Object>>() {});
                    // 移除敏感欄位與 null 欄位
                    map.entrySet().removeIf(entry ->
                            excludedFields.contains(entry.getKey()) || entry.getValue() == null
                    );
                    filteredList.add(map);
                } catch (IllegalArgumentException e) {
                    // 如果轉換失敗，就以 toString 方式處理
                    filteredList.add(Map.of("value", arg.toString()));
                }
            } else {
                // 字串、基本型別等直接包裝存入
                filteredList.add(Map.of("value", arg));
            }
        }

        try {
            String json = mapper.writeValueAsString(filteredList);
            // 限制長度，避免 log 過大
            return json.length() > 1000 ? json.substring(0, 1000) + "...[truncated]" : json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
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

