package cn.youngkbt.generic.base.service;

import cn.youngkbt.generic.base.dto.user.UserInsertDTO;
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

    /**
     * 更新用户在该项目的角色信息
     * @param username 更新的用户名
     * @param projectId 更新角色所在的项目 id
     * @param roleCode 更新的角色
     * @return true：删除成功，false：删除失败
     */
    public boolean updateGenericUserRole(String username ,Integer projectId, String roleCode);

    /**
     * 添加成员到 UserProject 表
     * @param projectId 项目 id
     * @param userInsertDTOList 成员信息
     * @return true：删除成功，false：删除失败
     */
    public boolean insertGenericUserProject(Integer projectId, List<UserInsertDTO> userInsertDTOList);

    /**
     * 删除项目里一个成员
     * @param username 成员用户名
     * @param projectId 项目 id
     * @return true：删除成功，false：删除失败
     */
    public boolean removeOneMember(String username, Integer projectId);

    /**
     * 删除项目的所有成员
     * @param projectId 项目 id
     * @return true：删除成功，false：删除失败
     */
    public boolean removeAllMember(Integer projectId);
}
