package cn.youngkbt.generic.security;

import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Kele-Bingtang
 * @date 2022/12/10 23:45
 * @note 登录认证成功处理器类
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 设置客户端的响应的内容类型
        response.setContentType("application/json;charset=UTF-8");

        // 获取当登录用户信息
        User user = (User) authentication.getPrincipal();

        // 生成 token
        String token = JwtTokenUtils.generateToken(authentication);

        LOGGER.info("用户 {} 登录成功，token 为 {}", user.getUsername(), token);

        // 获取输出流
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.println(new ObjectMapper().writeValueAsString(HttpResult.ok(token)));
        writer.flush();
        writer.close();
    }
}
