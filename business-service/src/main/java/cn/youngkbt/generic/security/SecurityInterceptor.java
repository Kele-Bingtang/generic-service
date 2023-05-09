package cn.youngkbt.generic.security;

import cn.youngkbt.generic.common.http.HttpResult;
import cn.youngkbt.generic.common.http.ResponseStatusEnum;
import cn.youngkbt.generic.common.utils.StringUtils;
import cn.youngkbt.generic.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author Kele-Bingtang
 * @date 2023/4/18 22:33
 * @note
 */
public class SecurityInterceptor implements HandlerInterceptor {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String secretKey = request.getHeader("Secret-Key");
        if (StringUtils.isBlank(secretKey)) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println(new ObjectMapper().writeValueAsString(HttpResult.response(null, ResponseStatusEnum.NO_PERMISSION)));
            writer.flush();
            writer.close();
            return false;
        } else {
            userService.queryUserRole(secretKey);
            return true;
        }
    }
}
