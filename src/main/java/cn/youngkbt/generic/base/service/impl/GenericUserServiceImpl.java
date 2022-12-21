package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.GenericUserMapper;
import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.GenericRole;
import cn.youngkbt.generic.base.model.GenericUser;
import cn.youngkbt.generic.base.model.UserProject;
import cn.youngkbt.generic.base.service.GenericProjectService;
import cn.youngkbt.generic.base.service.GenericRoleService;
import cn.youngkbt.generic.base.service.GenericUserService;
import cn.youngkbt.generic.base.service.UserProjectService;
import cn.youngkbt.generic.exception.ConditionSqlException;
import cn.youngkbt.generic.utils.ObjectUtils;
import cn.youngkbt.generic.utils.SearchUtils;
import cn.youngkbt.generic.utils.SecurityUtils;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
@Service
public class GenericUserServiceImpl implements GenericUserService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GenericUserMapper genericUserMapper;
    @Resource
    private GenericProjectService genericProjectService;
    @Resource
    private GenericRoleService genericRoleService;
    @Resource
    private UserProjectService userProjectService;

    @Override
    public List<GenericUser> queryGenericUserByConditions(List<ConditionVo> conditionVos) {
        QueryWrapper<GenericUser> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericUser.class);
        try {
            return genericUserMapper.selectList(queryWrapper);
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    public List<GenericUser> queryAllMemberNotInProject(String secretKey) {
        return userProjectService.queryAllMemberNotInProject(secretKey);
    }

    @Override
    public IPage<GenericUser> queryGenericUserListPages(IPage<GenericUser> page, GenericUser genericUser) {
        QueryWrapper<GenericUser> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(genericUser);
        try {
            return genericUserMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    public GenericUser findByUsername(String username) {
        QueryWrapper<GenericUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return genericUserMapper.selectOne(queryWrapper);
    }

    @Override
    public GenericRole queryGenericUserRole(String secretKey) {
        String username = SecurityUtils.getUsername();
        return genericRoleService.queryGenericRoleByUserAndProject(username, secretKey);
    }

    @Override
    public IPage<GenericUser> queryGenericUserConditionsPages(IPage<GenericUser> page, List<ConditionVo> conditionVos) {
        QueryWrapper<GenericUser> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericUser.class);
        try {
            return genericUserMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    public List<GenericUser> queryGenericMemberInProject(String secretKey, Integer pageNo, Integer pageSize) {
        Integer currentPage = (pageNo - 1) * pageSize;
        return userProjectService.queryGenericMemberInProject(secretKey, currentPage, pageSize);
    }

    @Override
    public GenericUser insertGenericUser(GenericUser genericUser) {
        int i = genericUserMapper.insert(genericUser);
        if (i == 0) {
            return null;
        }
        return genericUser;
    }

    @Override
    public GenericUser updateGenericUser(GenericUser genericUser) {
        int i = genericUserMapper.updateById(genericUser);
        if (i == 0) {
            return null;
        }
        return genericUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenericUser deleteGenericUserById(GenericUser genericUser) {
        // 删除用户下的所有项目
        QueryWrapper<GenericProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_user", genericUser.getId());
        genericProjectService.deleteGenericProjectByColumns(queryWrapper);
        int i = genericUserMapper.deleteById(genericUser);
        if (i == 0) {
            return null;
        }
        return genericUser;
    }

    @Override
    public boolean updateGenericUserRole(String username, Integer projectId, String roleCode) {
        GenericRole genericRole = genericRoleService.queryGenericRoleByCode(roleCode);
        if (ObjectUtils.isEmpty(genericRole) && ObjectUtils.isEmpty(genericRole.getId())) {
            return false;
        }
        UpdateWrapper<UserProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", username).eq("project_id", projectId).set("role_id", genericRole.getId());
        return userProjectService.update(updateWrapper);
    }

    @Override
    public boolean insertGenericUserProject(Integer projectId, List<GenericUser> userList) {
        List<UserProject> userProjectList = new ArrayList<>();
        userList.forEach(user -> {
            UserProject userProject = new UserProject();
            userProject.setUsername(user.getUsername());
            userProject.setProjectId(projectId);
            userProject.setRoleId(2);
            userProject.setEnterType(1);
            userProjectList.add(userProject);
        });
        return userProjectService.saveBatch(userProjectList);
    }

    @Override
    public boolean removeOneMember(String username, Integer projectId) {
        UpdateWrapper<UserProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", username).eq("project_id", projectId);
        return userProjectService.remove(updateWrapper);
    }

}