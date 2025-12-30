package com.ding.spring_001.common;

import cn.hutool.core.util.StrUtil;
import com.ding.spring_001.entity.User;
import com.ding.spring_001.exception.CustomException;
import com.ding.spring_001.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * jwt拦截器 - 在请求访问后台时先进行验证token
 * 
 * @author hxy
 * @date 2023/9/22
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行：允许无 token 的静态文件获取请求（仅放行直接访问单个文件的 GET 请求）
        try {
            String method = request.getMethod();
            String uri = request.getRequestURI();
            // 仅对 GET 且匹配 /api/files/{flag} 形式的请求放行，避免放行 /api/files/upload 或 /api/files/uploadAll
            if ("GET".equalsIgnoreCase(method) && uri.matches("^/api/files/(?!upload$|uploadAll$).+$")) {
                return true;
            }
        } catch (Exception ignore) {
            // 忽略匹配异常，继续走正常认证逻辑
        }

        // 1. 从请求头中获取token
        String token = request.getHeader("token");
        // 担心你将token直接放在参数中的
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }
        // 2.判断token是否存在
        if (StrUtil.isBlank(token)) { // 如果length为0
            System.out.println("登录失效,请重新登录");
            throw new CustomException("登录失效,请重新登录");
        }
        // 3.获取token中的载荷=userid
        String userId;

        User user = null;
        try {
            // 解码
            userId = JWT.decode(token).getAudience().get(0);
            // 如果是本地 admin（id=0），直接构造一个临时用户（不查 DB）
            if ("0".equals(userId)) {
                user = new User();
                user.setId(0);
                user.setUsername("admin");
                user.setPassword("admin123");
            } else {
                // 根据token得到用户的userId，查询数据库中的用户信息
                user = userService.findById(Integer.parseInt(userId));
            }
        } catch (Exception e) { // 抓父亲 子类也都被抓住
            System.out.println("--------------------这里捕获了");
            String errMsg = "登录认证token失败,请重新登录";
            log.error(errMsg + ",token=" + token, e);
            System.out.println("登录认证token失败,请重新登录");
            throw new CustomException(errMsg);
        }
        if (user == null) {
            System.out.println("用户不存在");
            throw new CustomException("用户不存在");
        }
        try {
            // 通过密码签名验证 token 的有效性
            JWTVerifier build = JWT.require(Algorithm.HMAC512(user.getPassword())).build();
            // 验证token
            build.verify(token);
        } catch (JWTVerificationException e) {
            System.out.println("token认证失败,请重新登录");
            throw new CustomException("token认证失败,请重新登录");
        }
        return true;
    }
}