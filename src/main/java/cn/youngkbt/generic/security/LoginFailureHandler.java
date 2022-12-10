package cn.youngkbt.generic.security;

import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.ResponseStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Kele-Bingtang
 * @date 2022/12/10 23:51
 * @note 用户认证失败处理类
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 设置客户端响应编码格式
        response.setContentType("application/json;charset=UTF-8");
        // 获取输出流
        PrintWriter writer = response.getWriter();
        // 判断异常类型
        ResponseStatusEnum responseStatusEnum = ResponseStatusEnum.LOGIN_FAIL;
        if (exception instanceof AccountExpiredException) {
            responseStatusEnum = ResponseStatusEnum.USER_ACCOUNT_EXPIRED;
        } else if (exception instanceof BadCredentialsException) {
            responseStatusEnum = ResponseStatusEnum.USERNAME_PASSWORD_ERROR;
        } else if (exception instanceof CredentialsExpiredException) {
            responseStatusEnum = ResponseStatusEnum.USER_PASSWORD_EXPIRED;
        } else if (exception instanceof DisabledException) {
            responseStatusEnum = ResponseStatusEnum.USER_ACCOUNT_DISABLE;
        } else if (exception instanceof LockedException) {
            responseStatusEnum = ResponseStatusEnum.USER_ACCOUNT_LOCKED;
        } else if (exception instanceof InternalAuthenticationServiceException) {
            responseStatusEnum = ResponseStatusEnum.USER_ACCOUNT_NOT_EXIST;
        }
        LOGGER.error("Exception：{}", responseStatusEnum.getMessage());
        // 将错误信息转换成 JSON
        writer.println(new ObjectMapper().writeValueAsString(HttpResult.processResult(null, responseStatusEnum)));
        writer.flush();
        writer.close();
    }
}