package cn.youngkbt.generic.security;

import cn.youngkbt.generic.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Kele-Bingtang
 * @date 2022/12/10 21:43
 * @note 用户访问项目的资源前，先进行验证 token
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 访问项目前，获取 request 的 token, 并检查登录状态
        SecurityUtils.checkAuthentication(request);
        // 释放目标资源到其他拦截器
        chain.doFilter(request, response);
    }
}