package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.dto.user.UserInsertDTO;
import cn.youngkbt.generic.base.mapper.UserProjectMapper;
import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.GenericRole;
import cn.youngkbt.generic.base.model.GenericUser;
import cn.youngkbt.generic.base.model.UserProject;
import cn.youngkbt.generic.base.service.RoleService;
import cn.youngkbt.generic.base.service.UserProjectService;
import cn.youngkbt.generic.utils.ObjectUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022/12/21 21:56
 * @note
 */
@Service
public class UserProjectServiceImpl extends ServiceImpl<UserProjectMapper, UserProject> implements UserProjectService {

    @Resource
    private UserProjectMapper userProjectMapper;
    @Resource
    private RoleService roleService;
    private final String USERNAME = "username";

    @Override
    public List<GenericProject> queryGenericProjectListOwner(UserProject userProject) {
        return userProjectMapper.queryGenericProjectListOwner(userProject);
    }

    @Override
    public List<GenericUser> queryGenericMemberInProject(String secretKey, Integer currentPage, Integer pageSize) {
        return userProjectMapper.queryMemberInProject(secretKey, currentPage, pageSize);
    }

    @Override
    public List<GenericUser> queryAllMemberNotInProject(String secretKey) {
        return userProjectMapper.queryAllMemberNotInProject(secretKey);
    }


    @Override
    public boolean updateGenericUserRole(String username, Integer projectId, String roleCode) {
        GenericRole genericRole = roleService.queryGenericRoleByCode(roleCode);
        if (ObjectUtils.isEmpty(genericRole) && ObjectUtils.isEmpty(genericRole.getId())) {
            return false;
        }
        UpdateWrapper<UserProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(USERNAME, username).eq("project_id", projectId).set("role_id", genericRole.getId());
        return this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertGenericUserProject(Integer projectId, List<UserInsertDTO> userInsertDTOList) {
        List<UserProject> userProjectList = new ArrayList<>();
        userInsertDTOList.forEach(user -> {
            UserProject userProject = new UserProject();
            userProject.setUsername(user.getUsername());
            userProject.setProjectId(projectId);
            userProject.setRoleId(2);
            userProject.setEnterType(1);
            userProjectList.add(userProject);
        });
        return this.saveBatch(userProjectList);
    }

    @Override
    public boolean removeOneMember(String username, Integer projectId) {
        UpdateWrapper<UserProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(USERNAME, username).eq("project_id", projectId);
        int result = userProjectMapper.delete(updateWrapper);
        return result != 0;
    }

    @Override
    public boolean removeAllMember(Integer projectId) {
        int result = userProjectMapper.deleteById(projectId);
        return result != 0;
    }
}
