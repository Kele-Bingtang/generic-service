package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.dto.user.UserDeleteDTO;
import cn.youngkbt.generic.base.dto.user.UserInsertDTO;
import cn.youngkbt.generic.base.dto.user.UserQueryDTO;
import cn.youngkbt.generic.base.dto.user.UserUpdateDTO;
import cn.youngkbt.generic.base.mapper.UserMapper;
import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.GenericRole;
import cn.youngkbt.generic.base.model.GenericUser;
import cn.youngkbt.generic.base.service.ProjectService;
import cn.youngkbt.generic.base.service.RoleService;
import cn.youngkbt.generic.base.service.UserProjectService;
import cn.youngkbt.generic.base.service.UserService;
import cn.youngkbt.generic.base.vo.RoleVO;
import cn.youngkbt.generic.base.vo.UserVO;
import cn.youngkbt.generic.exception.GenericException;
import cn.youngkbt.generic.http.ResponseStatusEnum;
import cn.youngkbt.generic.utils.Assert;
import cn.youngkbt.generic.utils.ObjectUtils;
import cn.youngkbt.generic.utils.SecurityUtils;
import cn.youngkbt.generic.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.BeanUtils;
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

    @Override
    public List<UserVO> queryUserListPages(IPage<GenericUser> page, UserQueryDTO userQueryDTO) {
        GenericUser user = new GenericUser();
        BeanUtils.copyProperties(userQueryDTO, user);
        QueryWrapper<GenericUser> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(user);
        try {
            IPage<GenericUser> userIPage = userMapper.selectPage(page, queryWrapper);
            return this.getUserVOList(userIPage.getRecords());
        } catch (Exception e) {
            throw new GenericException(ResponseStatusEnum.CONDITION_SQL_ERROR);
        }
    }

    @Override
    public UserVO findByUsername(String username) {
        QueryWrapper<GenericUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(USERNAME, username);
        GenericUser user = userMapper.selectOne(queryWrapper);
        Assert.isTrue(ObjectUtils.isNotEmpty(user) || StringUtils.isNotBlank(user.getUsername()), "用户信息不存在");
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public RoleVO queryUserRole(String secretKey) {
        String username = SecurityUtils.getUsername();
        GenericRole role = roleService.queryGenericRoleByUserAndProject(username, secretKey);
        Assert.isTrue(ObjectUtils.isNotEmpty(role) && ObjectUtils.isNotEmpty(role.getCode()), "没有权限");
        RoleVO roleVO = new RoleVO();
        roleVO.setCode(role.getCode());
        roleVO.setName(role.getName());
        return roleVO;
    }

    @Override
    public List<UserVO> queryMemberInProject(String secretKey, Integer pageNo, Integer pageSize) {
        Integer currentPage = (pageNo - 1) * pageSize;
        List<GenericUser> userList = userProjectService.queryGenericMemberInProject(secretKey, currentPage, pageSize);
        List<UserVO> userVoList = new ArrayList<>();
        userList.forEach(user -> {
            UserVO userVo = new UserVO();
            RoleVO roleVo = new RoleVO();
            BeanUtils.copyProperties(user, userVo);
            // user 对象有 role 对象，BeanUtils 无法转换对象里的对象，所以只能手动转
            roleVo.setCode(user.getGenericRole().getCode());
            roleVo.setName(user.getGenericRole().getName());
            userVo.setRole(roleVo);
            userVoList.add(userVo);
        });
        return userVoList;
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
        if(ObjectUtils.isNotEmpty(genericUser) && genericUser.getId() > 0) {
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

    public List<UserVO> getUserVOList(List<GenericUser> userList) {
        List<UserVO> userVOList = new ArrayList<>();
        userList.forEach(user -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);
            userVOList.add(vo);
        });
        return userVOList;
    }

    public String response(int result, String returnMessage) {
        if (result == 0) {
            throw new GenericException(ResponseStatusEnum.FAIL);
        }
        return returnMessage;
    }

}