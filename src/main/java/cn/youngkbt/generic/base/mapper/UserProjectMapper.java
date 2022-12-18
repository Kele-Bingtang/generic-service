package cn.youngkbt.generic.base.mapper;

import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.UserProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022/12/18 21:23
 * @note
 */
@Mapper
public interface UserProjectMapper extends BaseMapper<UserProject> {

    public List<GenericProject> queryGenericProjectListOwner(String username);
    
}
