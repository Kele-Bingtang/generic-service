package cn.youngkbt.generic.security;

import cn.youngkbt.generic.common.http.HttpResult;
import cn.youngkbt.generic.common.model.GenericUser;
import cn.youngkbt.generic.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Kele-Bingtang
 * @date 2023/4/2 22:09
 * @note 退出系统成功处理类
 */
public class ExitSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    @Resource
    private UserMapper userMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 获取当登录用户信息
        User user = (User) authentication.getPrincipal();

        UpdateWrapper<GenericUser> updateWrapper = new UpdateWrapper<>();
        // 修改用户状态为 离线
        updateWrapper.set("username", user.getUsername()).eq("status", 0);
        userMapper.update(null, updateWrapper);

        // 获取输出流
        response.setCharacterEncoding("UTF-8");
        // 设置客户端的响应的内容类型
        response.setContentType("application/json;charset=UTF-8");
        
        PrintWriter writer = response.getWriter();
        writer.print(new ObjectMapper().writeValueAsString(HttpResult.ok("退出成功")));
        writer.flush();
        writer.close();

    }
}
