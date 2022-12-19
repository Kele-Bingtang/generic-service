package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.GenericProjectMapper;
import cn.youngkbt.generic.base.mapper.UserProjectMapper;
import cn.youngkbt.generic.base.model.GenericCategory;
import cn.youngkbt.generic.base.model.GenericProject;
import cn.youngkbt.generic.base.model.UserProject;
import cn.youngkbt.generic.base.service.GenericCategoryService;
import cn.youngkbt.generic.base.service.GenericProjectService;
import cn.youngkbt.generic.exception.ConditionSqlException;
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
    private UserProjectMapper userProjectMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<GenericProject> queryGenericProjectByConditions(List<ConditionVo> conditionVos) {
        QueryWrapper<GenericProject> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericProject.class);
        try {
            return genericProjectMapper.selectList(queryWrapper);
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    public List<GenericProject> queryGenericProjectList(GenericProject genericProject) {
        QueryWrapper<GenericProject> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(genericProject);
        List<GenericProject> projectList = genericProjectMapper.selectList(queryWrapper);
        return projectList;
    }

    @Override
    public List<GenericProject> queryGenericProjectListOwner(UserProject userProject) {
        String username = SecurityUtils.getUsername();
        String key = username + "_project_" + userProject.getEnterType();
        // 从 Redis 拿缓存
        Object o = redisTemplate.opsForValue().get(key);
        if (o instanceof List) {
            List<GenericProject> projectList = (List<GenericProject>) o;
            LOGGER.info("从 Redis 拿到数据：{}", projectList);
            return projectList;
        }
        userProject.setUsername(username);
        List<GenericProject> projectList = userProjectMapper.queryGenericProjectListOwner(userProject);
        redisTemplate.opsForValue().set(key, projectList, 24, TimeUnit.HOURS);
        return projectList;
    }

    @Override
    public IPage<GenericProject> queryGenericProjectListPages(IPage<GenericProject> page, GenericProject genericProject) {
        String key = SecurityUtils.getUsername() + "_project_" + page.getCurrent() + "_" + page.getSize() + "_" + genericProject.toString();
        // 从 Redis 拿缓存
        Object o = redisTemplate.opsForValue().get(key);
        if (o instanceof IPage) {
            IPage<GenericProject> projectPage = (IPage<GenericProject>) o;
            LOGGER.info("从 Redis 拿到数据：{}", projectPage);
            return projectPage;
        }
        QueryWrapper<GenericProject> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(genericProject);
        try {
            IPage<GenericProject> projectPage = genericProjectMapper.selectPage(page, queryWrapper);
            // 存入 Redis
            redisTemplate.opsForValue().set(key, projectPage, 24, TimeUnit.HOURS);
            return projectPage;
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    public IPage<GenericProject> queryGenericProjectConditionsPages(IPage<GenericProject> page, List<ConditionVo> conditionVos) {
        QueryWrapper<GenericProject> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericProject.class);
        try {
            return genericProjectMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenericProject insertGenericProject(GenericProject genericProject) {
        // 不带 - 的 UUID 作为项目密钥
        String noBarUUID = SecureUtils.noBarUUID();
        genericProject.setSecretKey(noBarUUID);
        int i = genericProjectMapper.insert(genericProject);
        // 生成默认目录
        GenericCategory category = new GenericCategory();
        category.setCategoryName("默认目录");
        category.setProjectId(genericProject.getId());
        category.setCreateUser(genericProject.getCreateUser());
        category.setModifyUser(genericProject.getModifyUser());
        genericCategoryService.insertGenericCategory(category);
        // 添加到 user_project 表
        UserProject userProject = new UserProject();
        userProject.setUsername(SecurityUtils.getUsername());
        userProject.setProjectId(genericProject.getId());
        // 默认都是开发者
        userProject.setRoleId(2);
        userProject.setEnterType(0);
        userProjectMapper.insert(userProject);
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
        // 删除项目的目录
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
        // 获取将要删除项目的主键
        List<GenericProject> genericProjectList = genericProjectMapper.selectList(queryWrapper);
        if (!genericProjectList.isEmpty()) {
            List<Integer> ids = new ArrayList<>();
            genericProjectList.forEach(project -> ids.add(project.getId()));
            if (!ids.isEmpty()) {
                // 删除项目下的目录
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
                LOGGER.info("删除了 {} 个 key", delete);
            }
        }
    }
}