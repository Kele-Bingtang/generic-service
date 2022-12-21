package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.UserProjectMapper;
import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.GenericUser;
import cn.youngkbt.generic.base.model.UserProject;
import cn.youngkbt.generic.base.service.UserProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
