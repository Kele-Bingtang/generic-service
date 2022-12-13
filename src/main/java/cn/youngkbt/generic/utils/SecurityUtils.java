package cn.youngkbt.generic.utils;

import cn.youngkbt.generic.security.JwtAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kele-Bingtang
 * @date 2022/12/10 21:42
 * @note Spring Security 认证工具
 */
public class SecurityUtils {
    
    private SecurityUtils() {
    }

    /**
     * 系统登录认证
     *
     * @param request               客户端请求
     * @param username              当前用户名
     * @param password              当前用户密码
     * @param authenticationManager 认证对象
     * @return 认证后的用户信息，内容包括 token
     */
    public static JwtAuthenticationToken login(HttpServletRequest request, String username, String password, AuthenticationManager authenticationManager) {
        JwtAuthenticationToken token = new JwtAuthenticationToken(username, password);
        // token 存入 request 的相关信息
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // 执行登录认证过程，获取调用 UserDetailsServiceImpl 的 loadUserByUsername 方法
        Authentication authentication = authenticationManager.authenticate(token);
        // 认证成功存储认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌并返回给客户端 
        token.setToken(JwtTokenUtils.generateToken(authentication));
        return token;
    }

    /**
     * 获取令牌进行认证
     *
     * @param request 客户端请求
     */
    public static void checkAuthentication(HttpServletRequest request) {
        // 获取令牌并根据令牌获取登录认证信息
        Authentication authentication = JwtTokenUtils.getAuthenticationeFromToken(request);
        // 设置登录认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 从上下文获取当前用户名
     *
     * @return 当前用户名
     */
    public static String getUsername() {
        String username = null;
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal != null && principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                return String.valueOf(authentication);
            }
        }
        return username;
    }

    /**
     * 从传入的认证信息中获取用户名
     *
     * @return 用户名
     */
    public static String getUsername(Authentication authentication) {
        String username = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal != null && principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                return String.valueOf(authentication);
            }
        }
        return username;
    }

    /**
     * 从上下文获取当前登录信息
     *
     * @return 当前登录信息
     */
    public static Authentication getAuthentication() {
        if (SecurityContextHolder.getContext() == null) {
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
