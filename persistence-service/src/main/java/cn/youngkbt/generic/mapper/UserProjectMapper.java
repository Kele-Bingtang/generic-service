package cn.youngkbt.generic.mapper;

import cn.youngkbt.generic.common.model.GenericProject;
import cn.youngkbt.generic.common.model.GenericUser;
import cn.youngkbt.generic.common.model.UserProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022/12/18 21:23
 * @note
 */
@Mapper
public interface UserProjectMapper extends BaseMapper<UserProject> {

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
    public List<GenericUser> queryMemberInProject(@Param("secretKey") String secretKey, @Param("currentPage") Integer currentPage, @Param("pageSize") Integer pageSize);

    /**
     * 查询不在这个项目的所有成员
     * @param secretKey 项目密钥
     * @return 不在这个项目的所有成员
     */
    public List<GenericUser> queryAllMemberNotInProject(String secretKey);
    
}
