package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.GenericProjectMapper;
import cn.youngkbt.generic.base.model.GenericCategory;
import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.GenericRole;
import cn.youngkbt.generic.base.model.UserProject;
import cn.youngkbt.generic.base.service.GenericCategoryService;
import cn.youngkbt.generic.base.service.GenericProjectService;
import cn.youngkbt.generic.base.service.GenericRoleService;
import cn.youngkbt.generic.exception.ExecuteSqlException;
import cn.youngkbt.generic.utils.ObjectUtils;
import cn.youngkbt.generic.utils.SearchUtils;
import cn.youngkbt.generic.utils.SecureUtils;
import cn.youngkbt.generic.utils.SecurityUtils;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Kele-Bingtang
 * @note 1.0
 * @date 2022-12-03 22:45:22
 */
@Service
public class GenericProjectServiceImpl implements GenericProjectService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GenericProjectMapper genericProjectMapper;
    @Resource
    private GenericCategoryService genericCategoryService;
    @Resource
    private GenericRoleService genericRoleService;
    @Resource
    private UserProjectServiceImpl userProjectService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<GenericProject> queryGenericProjectByConditions(List<ConditionVo> conditionVos) {
        QueryWrapper<GenericProject> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericProject.class);
        try {
            return genericProjectMapper.selectList(queryWrapper);
        } catch (Exception e) {
            throw new ExecuteSqlException();
        }
    }

    @Override
    public GenericProject queryGenericOneProjectBySecretKey(String secretKey) {
        QueryWrapper<GenericProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("secret_key", secretKey);
        return genericProjectMapper.selectOne(queryWrapper);
    }

    @Override
    public List<GenericProject> queryGenericProjectListOwner(UserProject userProject) {
        String username = SecurityUtils.getUsername();
        String key = username + "_project_" + userProject.getEnterType();
        // ??? Redis ?????????
        Object o = redisTemplate.opsForValue().get(key);
        if (o instanceof List) {
            List<GenericProject> projectList = (List<GenericProject>) o;
            LOGGER.info("??? Redis ???????????????{}", projectList);
            return projectList;
        }
        userProject.setUsername(username);
        List<GenericProject> projectList = userProjectService.queryGenericProjectListOwner(userProject);
        redisTemplate.opsForValue().set(key, projectList, 24, TimeUnit.HOURS);
        return projectList;
    }

    @Override
    public IPage<GenericProject> queryGenericProjectListPages(IPage<GenericProject> page, GenericProject genericProject) {
        String key = SecurityUtils.getUsername() + "_project_" + page.getCurrent() + "_" + page.getSize() + "_" + genericProject.toString();
        // ??? Redis ?????????
        Object o = redisTemplate.opsForValue().get(key);
        if (o instanceof IPage) {
            IPage<GenericProject> projectPage = (IPage<GenericProject>) o;
            LOGGER.info("??? Redis ???????????????{}", projectPage);
            return projectPage;
        }
        QueryWrapper<GenericProject> queryWrapper = new QueryWrapper<>();
        // ?????? genericCategory ????????????????????????????????????
        queryWrapper.setEntity(genericProject);
        try {
            IPage<GenericProject> projectPage = genericProjectMapper.selectPage(page, queryWrapper);
            // ?????? Redis
            redisTemplate.opsForValue().set(key, projectPage, 24, TimeUnit.HOURS);
            return projectPage;
        } catch (Exception e) {
            throw new ExecuteSqlException();
        }
    }

    @Override
    public IPage<GenericProject> queryGenericProjectConditionsPages(IPage<GenericProject> page, List<ConditionVo> conditionVos) {
        QueryWrapper<GenericProject> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericProject.class);
        try {
            return genericProjectMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            throw new ExecuteSqlException();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenericProject insertGenericProject(GenericProject genericProject) {
        // ?????? - ??? UUID ??????????????????
        String noBarUUID = SecureUtils.noBarUUID();
        genericProject.setSecretKey(noBarUUID);
        int i = genericProjectMapper.insert(genericProject);
        // ??????????????????
        GenericCategory category = new GenericCategory();
        category.setCategoryName("????????????");
        category.setProjectId(genericProject.getId());
        category.setCreateUser(genericProject.getCreateUser());
        category.setModifyUser(genericProject.getModifyUser());
        genericCategoryService.insertGenericCategory(category);
        // ????????? user_project ???
        UserProject userProject = new UserProject();
        userProject.setUsername(SecurityUtils.getUsername());
        userProject.setProjectId(genericProject.getId());
        // ???????????????????????????
        GenericRole admin = genericRoleService.queryGenericRoleByCode("admin");
        Integer adminId = admin.getId();
        if(ObjectUtils.isNotEmpty(adminId)) {
            userProject.setRoleId(adminId);
        }else {
            userProject.setProjectId(1);
        }
        userProject.setEnterType(0);
        userProjectService.save(userProject);
        if (i == 0) {
            return null;
        } else {
            this.deleteCachedKeys();
        }
        return genericProject;
    }

    @Override
    public GenericProject updateGenericProject(GenericProject genericProject) {
        int i = genericProjectMapper.updateById(genericProject);
        if (i == 0) {
            return null;
        } else {
            this.deleteCachedKeys();
        }
        return genericProject;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenericProject deleteGenericProjectById(GenericProject genericProject) {
        QueryWrapper<GenericCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", genericProject.getId());
        // ?????????????????????
        genericCategoryService.deleteGenericServiceByColumns(queryWrapper);
        int i = genericProjectMapper.deleteById(genericProject);
        if (i == 0) {
            return null;
        } else {
            this.deleteCachedKeys();
        }
        return genericProject;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteGenericProjectByColumns(QueryWrapper<GenericProject> queryWrapper) {
        queryWrapper.select("id");
        // ?????????????????????????????????
        List<GenericProject> genericProjectList = genericProjectMapper.selectList(queryWrapper);
        if (!genericProjectList.isEmpty()) {
            List<Integer> ids = new ArrayList<>();
            genericProjectList.forEach(project -> ids.add(project.getId()));
            if (!ids.isEmpty()) {
                // ????????????????????????
                genericCategoryService.deleteGenericServiceByIds(ids);
            }
        }
        int i = genericProjectMapper.delete(queryWrapper);
        if (i > 0) {
            this.deleteCachedKeys();
        }
        return i;
    }

    public void deleteCachedKeys() {
        Set<String> keys = redisTemplate.keys(SecurityUtils.getUsername() + "_project*");
        if (null != keys) {
            Long delete = redisTemplate.delete(keys);
            if (null != delete) {
                LOGGER.info("????????? {} ??? key", delete);
            }
        }
    }
}