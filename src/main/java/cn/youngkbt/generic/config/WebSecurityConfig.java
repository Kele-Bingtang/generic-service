package cn.youngkbt.generic.config;

import cn.youngkbt.generic.security.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author Kele-Bingtang
 * @date 2022/12/10 21:52
 * @note
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Resource
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 启用加密方式
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用 csrf, 由于使用的是 JWT，我们这里不需要 csrf
        http.cors().and().csrf().disable()
                .authorizeRequests()
                // 跨域预检请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 首页和登录页面放行
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/generic-api/**").permitAll()
                // 其他所有请求需要身份认证
                .anyRequest().authenticated()
                .and().headers().frameOptions().disable()
                .and().formLogin().loginPage("/login").loginProcessingUrl("/login")
                // 登录成功后的返回结果
                .successHandler(new LoginSuccessHandler())
                //登录失败后的返回结果
                .failureHandler(new LoginFailureHandler())
                // 退出登录处理器
                .and().logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler());
        
        // token 验证过滤器
        http.addFilterBefore(new JwtAuthenticationFilter(super.authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }
    /**
     * 加密类可以被 Autowired
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
