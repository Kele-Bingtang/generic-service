package cn.youngkbt.generic.service.impl;

import cn.youngkbt.generic.common.dto.user.UserDeleteDTO;
import cn.youngkbt.generic.common.dto.user.UserInsertDTO;
import cn.youngkbt.generic.common.dto.user.UserQueryDTO;
import cn.youngkbt.generic.common.dto.user.UserUpdateDTO;
import cn.youngkbt.generic.common.exception.GenericException;
import cn.youngkbt.generic.common.http.ResponseStatusEnum;
import cn.youngkbt.generic.common.model.GenericProject;
import cn.youngkbt.generic.common.model.GenericRole;
import cn.youngkbt.generic.common.model.GenericService;
import cn.youngkbt.generic.common.model.GenericUser;
import cn.youngkbt.generic.common.utils.Assert;
import cn.youngkbt.generic.common.utils.ObjectUtils;
import cn.youngkbt.generic.security.JwtTokenUtils;
import cn.youngkbt.generic.security.SecurityUtils;
import cn.youngkbt.generic.common.utils.StringUtils;
import cn.youngkbt.generic.mapper.UserMapper;
import cn.youngkbt.generic.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    private final String USERNAME = "username";

    @Resource
    private UserMapper userMapper;
    @Resource
    private ProjectService projectService;
    @Resource
    private RoleService roleService;
    @Resource
    private UserProjectService userProjectService;
    @Resource
    private ServiceService serviceService;

    @Override
    public List<GenericUser> queryUserListPages(IPage<GenericUser> page, UserQueryDTO userQueryDTO) {
        GenericUser user = new GenericUser();
        BeanUtils.copyProperties(userQueryDTO, user);
        QueryWrapper<GenericUser> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(user);
        try {
            IPage<GenericUser> userIPage = userMapper.selectPage(page, queryWrapper);
            return userIPage.getRecords();
        } catch (Exception e) {
            throw new GenericException(ResponseStatusEnum.CONDITION_SQL_ERROR);
        }
    }

    @Override
    public GenericUser findByUsername(String username) {
        QueryWrapper<GenericUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(USERNAME, username);
        GenericUser user = userMapper.selectOne(queryWrapper);
        Assert.isTrue(ObjectUtils.isNotEmpty(user) || StringUtils.isNotBlank(user.getUsername()), "用户信息不存在");
        return user;
    }

    @Override
    public GenericRole queryUserRole(String secretKey) {
        String username = SecurityUtils.getUsername();
        Assert.isTrue(StringUtils.isNotBlank(username), "没有权限");
        GenericRole role = roleService.queryRoleByUserAndProject(username, secretKey);
        Assert.isTrue(ObjectUtils.isNotEmpty(role) && ObjectUtils.isNotEmpty(role.getCode()), "没有权限");
        return role;
    }

    @Override
    public GenericRole queryUserRoleByToken(String secretKey, String serviceUrl, String token) {
        GenericService service = serviceService.queryServiceBySecretKeyAndUrl(secretKey, serviceUrl);
        if (service.getIsAuth() == 1) {
            Authentication authenticationFromToken = JwtTokenUtils.getAuthenticationFromToken(token);
            String username = SecurityUtils.getUsername(authenticationFromToken);
            Assert.isTrue(StringUtils.isNotBlank(username), "没有权限");
            GenericRole role = roleService.queryRoleByUserAndProject(username, secretKey);
            Assert.isTrue(ObjectUtils.isNotEmpty(role) && ObjectUtils.isNotEmpty(role.getCode()), "没有权限");
            return role;
        } else {
            return new GenericRole();
        }
    }

    @Override
    public List<GenericUser> queryMemberInProject(String secretKey, Integer pageNo, Integer pageSize) {
        Integer currentPage = (pageNo - 1) * pageSize;
        return userProjectService.queryMemberInProject(secretKey, currentPage, pageSize);
    }

    @Override
    public List<Map<String, Object>> queryAllMemberNotInProject(String secretKey) {
        List<GenericUser> userList = userProjectService.queryAllMemberNotInProject(secretKey);
        List<Map<String, Object>> userListMap = new ArrayList<>();
        userList.forEach(user -> {
            // 只传 username、email、status
            Map<String, Object> map = new HashMap<>();
            map.put(USERNAME, user.getUsername());
            map.put("email", user.getEmail());
            map.put("status", user.getStatus());
            userListMap.add(map);
        });
        return userListMap;
    }

    @Override
    public String insertUser(UserInsertDTO userInsertDTO) {
        // 先判断是否存在用户
        QueryWrapper<GenericUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userInsertDTO.getUsername());
        GenericUser genericUser = userMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(genericUser) && genericUser.getId() > 0) {
            throw new GenericException(ResponseStatusEnum.USER_ACCOUNT_EXISTED);
        }
        GenericUser user = new GenericUser();
        BeanUtils.copyProperties(userInsertDTO, user);
        int result = userMapper.insert(user);
        return this.response(result, "用户注册成功");
    }

    @Override
    public String updateUser(UserUpdateDTO userUpdateDTO) {
        GenericUser user = new GenericUser();
        BeanUtils.copyProperties(userUpdateDTO, user);
        int result = userMapper.updateById(user);
        return this.response(result, "修改信息成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteUserById(UserDeleteDTO userDeleteDTO) {
        GenericUser user = new GenericUser();
        BeanUtils.copyProperties(userDeleteDTO, user);
        // 删除用户下的所有项目
        QueryWrapper<GenericProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_user", user.getId());
        projectService.deleteProjectByColumns(queryWrapper);
        int result = userMapper.deleteById(user);
        return this.response(result, "用户注册成功");
    }

    public String response(int result, String returnMessage) {
        if (result == 0) {
            throw new GenericException(ResponseStatusEnum.FAIL);
        }
        return returnMessage;
    }

}