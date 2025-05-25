package com.example.springboot.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtTokenUtils {

    private static AdminService staticAdminService;
    private static EmployeeService staticEmployeeService;

    @Resource
    private AdminService adminService;

    @Resource
    private EmployeeService employeeService;

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtils.class);

    @PostConstruct
    public void setUserService() {
        staticAdminService = adminService;
        staticEmployeeService = employeeService;
    }

    // 生成 Token（包含 id 與角色）
    public static String genToken(String userId, String role, String sign) {
        return JWT.create()
                .withAudience(userId)
                .withClaim("role", role)
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))  // 2小時有效
                .sign(Algorithm.HMAC256(sign));
    }

    // 根據 Token 中的角色與 id 返回對應的登入使用者物件（Account）
    public static Account getCurrentAccount() {
        String token = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//            token = request.getHeader("token");

            token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                token = request.getParameter("token");
            }
            if (StrUtil.isBlank(token)) {
                log.debug("未找到token，用戶未登錄或會話已過期");
                return null;
            }


            DecodedJWT jwt = JWT.decode(token);
            String id = jwt.getAudience().getFirst();
            String role = jwt.getClaim("role").asString();

            if ("Admin".equalsIgnoreCase(role)) {
                return staticAdminService.selectById(Integer.valueOf(id));
            } else if ("User".equalsIgnoreCase(role)) {
                return staticEmployeeService.selectById(Integer.valueOf(id));
            } else {
                log.error("未知角色: {}", role);
                return null;
            }
        } catch (Exception e) {
            log.error("获取当前登录用户信息失败, token={}", token, e);
            return null;
        }
    }
}

