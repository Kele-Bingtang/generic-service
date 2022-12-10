package cn.youngkbt.generic.security;

import cn.youngkbt.generic.base.model.GenericUser;
import cn.youngkbt.generic.base.service.GenericUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022/12/10 21:45
 * @note 该类是 Spring Security 自动调用，判断用户是否存在、有权限
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private GenericUserService genericUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        GenericUser user = genericUserService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        // 用户权限列表，根据用户拥有的权限标识与如 @PreAuthorize("hasAuthority('sys:menu:view')") 标注的接口对比，决定是否可以调用接口
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}