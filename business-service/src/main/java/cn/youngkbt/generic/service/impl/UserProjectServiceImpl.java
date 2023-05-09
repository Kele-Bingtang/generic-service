package cn.youngkbt.generic.service.impl;

import cn.youngkbt.generic.common.dto.user.UserInsertDTO;
import cn.youngkbt.generic.common.model.GenericProject;
import cn.youngkbt.generic.common.model.GenericRole;
import cn.youngkbt.generic.common.model.GenericUser;
import cn.youngkbt.generic.common.model.UserProject;
import cn.youngkbt.generic.common.utils.ObjectUtils;
import cn.youngkbt.generic.mapper.UserProjectMapper;
import cn.youngkbt.generic.service.RoleService;
import cn.youngkbt.generic.service.UserProjectService;
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
    public List<GenericProject> queryProjectListOwner(UserProject userProject) {
        return userProjectMapper.queryGenericProjectListOwner(userProject);
    }

    @Override
    public List<GenericUser> queryMemberInProject(String secretKey, Integer currentPage, Integer pageSize) {
        return userProjectMapper.queryMemberInProject(secretKey, currentPage, pageSize);
    }

    @Override
    public List<GenericUser> queryAllMemberNotInProject(String secretKey) {
        return userProjectMapper.queryAllMemberNotInProject(secretKey);
    }


    @Override
    public boolean updateUserRole(String username, Integer projectId, String roleCode) {
        GenericRole genericRole = roleService.queryRoleByCode(roleCode);
        if (ObjectUtils.isEmpty(genericRole) && ObjectUtils.isEmpty(genericRole.getId())) {
            return false;
        }
        UpdateWrapper<UserProject> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(USERNAME, username).eq("project_id", projectId).set("role_id", genericRole.getId());
        return this.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUserProject(Integer projectId, List<UserInsertDTO> userInsertDTOList) {
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
