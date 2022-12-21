package cn.youngkbt.generic.base.service;

import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.GenericUser;
import cn.youngkbt.generic.base.model.UserProject;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022/12/21 21:55
 * @note
 */
public interface UserProjectService extends IService<UserProject> {

    /**
     * 查询用户所在的项目，包括：所有、创建的、加入的项目
     * @param userProject 项目对象
     * @return 用户所在的项目，包括：所有、创建的、加入的项目
     */
    public List<GenericProject> queryGenericProjectListOwner(UserProject userProject);

    /**
     * 通过项目密钥查询该项目的所有用户，包括用户的角色
     * @param secretKey 项目密钥
     * @param currentPage 当前页
     * @param pageSize 一页显示的数据
     * @return 该项目的所有用户，包括用户的角色
     */
    public List<GenericUser> queryGenericMemberInProject(String secretKey, Integer currentPage, Integer pageSize);

    /**
     * 查询不在这个项目的所有成员
     * @param secretKey 项目密钥
     * @return 不在这个项目的所有成员
     */
    public List<GenericUser> queryAllMemberNotInProject(String secretKey);
}
