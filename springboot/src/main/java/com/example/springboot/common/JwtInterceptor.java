package com.example.springboot.common;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Employee;
import com.example.springboot.exception.CustomException;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
/*攔截器*/
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Resource
    private AdminService adminService;

    @Resource
    private EmployeeService employeeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 获取 token
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }

        // 2. 验证 token 是否为空
        if (StrUtil.isBlank(token)) {
            throw new CustomException("500", "无 token，请重新登录");
        }

        String userId;
        String role;
        try {
            DecodedJWT jwt = JWT.decode(token);
            userId = jwt.getAudience().get(0);
            role = jwt.getClaim("role").asString(); // 建议登入时签发 token 时加上 role
        } catch (Exception e) {
            String errMsg = "token 解析失败，请重新登录";
            log.error(errMsg + ", token=" + token, e);
            throw new CustomException("500", errMsg);
        }

        // 3. 根據 role 查詢使用者並驗證 token
        Object user;
        String password;
        try {
            switch (role) {
                case "Admin":
                    Admin admin = adminService.selectById(Integer.parseInt(userId));
                    if (admin == null) {
                        throw new CustomException("500", "管理员不存在，请重新登录");
                    }
                    user = admin;
                    password = admin.getPassword();
                    break;
                case "User":
                    Employee employee = employeeService.selectById(Integer.parseInt(userId));
                    if (employee == null) {
                        throw new CustomException("500", "员工不存在，请重新登录");
                    }
                    user = employee;
                    password = employee.getPassword();
                    break;
                default:
                    throw new CustomException("500", "未知角色类型：" + role);
            }

            // 4. 驗證 token
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(password)).build();
            verifier.verify(token);

        } catch (JWTVerificationException e) {
            throw new CustomException("500", "token 验证失败，请重新登录");
        }

        log.info("验证成功，角色: {}, ID: {}", role, userId);
        return true;
    }
}
