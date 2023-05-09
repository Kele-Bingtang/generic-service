package cn.youngkbt.generic.service.impl;

import cn.youngkbt.generic.common.dto.category.CategoryInsertDTO;
import cn.youngkbt.generic.common.dto.project.ProjectDeleteDTO;
import cn.youngkbt.generic.common.dto.project.ProjectInsertDTO;
import cn.youngkbt.generic.common.dto.project.ProjectQueryOwnerDTO;
import cn.youngkbt.generic.common.dto.project.ProjectUpdateDTO;
import cn.youngkbt.generic.common.exception.GenericException;
import cn.youngkbt.generic.common.http.ResponseStatusEnum;
import cn.youngkbt.generic.common.model.GenericCategory;
import cn.youngkbt.generic.common.model.GenericProject;
import cn.youngkbt.generic.common.model.GenericRole;
import cn.youngkbt.generic.common.model.UserProject;
import cn.youngkbt.generic.common.utils.*;
import cn.youngkbt.generic.mapper.ApiMapper;
import cn.youngkbt.generic.mapper.ProjectMapper;
import cn.youngkbt.generic.security.SecurityUtils;
import cn.youngkbt.generic.service.CategoryService;
import cn.youngkbt.generic.service.ProjectService;
import cn.youngkbt.generic.service.RoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Kele-Bingtang
 * @note 1.0
 * @date 2022-12-03 22:45:22
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private CategoryService categoryService;
    @Resource
    private RoleService roleService;
    @Resource
    private UserProjectServiceImpl userProjectService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private ApiMapper apiMapper;

    @Override
    public GenericProject queryOneProjectBySecretKey(String secretKey) {
        QueryWrapper<GenericProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("secret_key", secretKey);
        GenericProject project = projectMapper.selectOne(queryWrapper);
        Assert.isTrue(ObjectUtils.isNotEmpty(project) || ObjectUtils.isNotEmpty(project.getId()), "密钥或者项目不存在");
        return project;
    }

    @Override
    public List<GenericProject> queryProjectListOwner(ProjectQueryOwnerDTO projectQueryOwnerDTO) {
        String username = SecurityUtils.getUsername();
        // String key = username + "_project_" + projectQueryOwnerDTO.getEnterType();
        // 从 Redis 拿缓存
        // Object o = redisTemplate.opsForValue().get(key);
        // if (o instanceof List) {
        //     List<ProjectVO> projectList = (List<ProjectVO>) o;
        //     LOGGER.info("从 Redis 拿到数据：{}", projectList);
        //     return projectList;
        // }
        UserProject userProject = new UserProject();
        userProject.setUsername(username);
        userProject.setEnterType(projectQueryOwnerDTO.getEnterType());
        List<GenericProject> projectList = userProjectService.queryProjectListOwner(userProject);
        // redisTemplate.opsForValue().set(key, projectList, 24, TimeUnit.HOURS);
        return projectList;
    }

    @Override
    public List<GenericProject> queryProjectListPages(IPage<GenericProject> page, ProjectQueryOwnerDTO projectQueryOwnerDTO) {
        GenericProject project = new GenericProject();
        BeanUtils.copyProperties(projectQueryOwnerDTO, project);
        String key = SecurityUtils.getUsername() + "_project_" + page.getCurrent() + "_" + page.getSize() + "_" + project.toString();
        // 从 Redis 拿缓存
        // Object o = redisTemplate.opsForValue().get(key);
        // if (o instanceof List) {
        //     List<ProjectVO> projectPage = (List<ProjectVO>) o;
        //     LOGGER.info("从 Redis 拿到数据：{}", projectPage);
        //     return projectPage;
        // }
        QueryWrapper<GenericProject> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(project);
        try {
            IPage<GenericProject> projectPage = projectMapper.selectPage(page, queryWrapper);
            List<GenericProject> projectList = projectPage.getRecords();
            // 存入 Redis
            // redisTemplate.opsForValue().set(key, projectList, 24, TimeUnit.HOURS);
            return projectList;
        } catch (Exception e) {
            throw new GenericException(ResponseStatusEnum.CONDITION_SQL_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertProject(ProjectInsertDTO projectInsertDTO) {
        // 先判断是否存在项目
        QueryWrapper<GenericProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_name", projectInsertDTO.getProjectName()).or().eq("base_url", projectInsertDTO.getBaseUrl());
        GenericProject genericProject = projectMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(genericProject) && genericProject.getId() > 0) {
            throw new GenericException(ResponseStatusEnum.PROJECT_EXEIT);
        }
        GenericProject project = new GenericProject();
        BeanUtils.copyProperties(projectInsertDTO, project);
        // 不带 - 的 UUID 作为项目密钥
        String noBarUUID = StringUtils.noBarUUID();
        project.setSecretKey(noBarUUID);
        int result = projectMapper.insert(project);
        // 生成默认目录
        CategoryInsertDTO categoryInsertDTO = new CategoryInsertDTO();
        categoryInsertDTO.setCategoryName("默认目录");
        categoryInsertDTO.setCategoryCode("default");
        categoryInsertDTO.setProjectId(project.getId());
        categoryInsertDTO.setCreateUser(project.getCreateUser());
        categoryInsertDTO.setModifyUser(project.getModifyUser());
        categoryService.insertCategory(categoryInsertDTO);
        // 添加到 user_project 表
        UserProject userProject = new UserProject();
        userProject.setUsername(SecurityUtils.getUsername());
        userProject.setProjectId(project.getId());
        // 默认创建者是管理员
        GenericRole admin = roleService.queryRoleByCode("admin");
        Integer adminId = admin.getId();
        if (ObjectUtils.isNotEmpty(adminId)) {
            userProject.setRoleId(adminId);
        } else {
            userProject.setProjectId(1);
        }
        userProject.setEnterType(0);
        userProjectService.save(userProject);
        return this.response(result, "插入成功");
    }

    @Override
    public String updateProject(ProjectUpdateDTO projectUpdateDTO) {
        GenericProject project = new GenericProject();
        BeanUtils.copyProperties(projectUpdateDTO, project);
        int result = projectMapper.updateById(project);
        return this.response(result, "更改成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteProjectById(ProjectDeleteDTO projectDeleteDTO) {
        GenericProject project = new GenericProject();
        BeanUtils.copyProperties(projectDeleteDTO, project);
        QueryWrapper<GenericCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", project.getId());
        // 删除项目的目录
        categoryService.deleteServiceByColumns(queryWrapper);
        // 删除项目成员
        userProjectService.removeAllMember(project.getId());
        // 删除项目
        int result = projectMapper.deleteById(project);
        return this.response(result, "删除成功");
    }

    @Override
    public List<String> queryDatabaseName(String databaseName) {
        List<String> databaseList = apiMapper.selectDatabaseInfo("show databases");
        if(!databaseList.isEmpty()) {
            // 去掉 MySQL 自带的表
            databaseList.remove("sys");
            databaseList.remove("information_schema");
            databaseList.remove("mysql");
            databaseList.remove("performance_schema");
        }
        return databaseList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteProjectByColumns(QueryWrapper<GenericProject> queryWrapper) {
        queryWrapper.select("id");
        // 获取将要删除项目的主键
        List<GenericProject> genericProjectList = projectMapper.selectList(queryWrapper);
        if (!genericProjectList.isEmpty()) {
            List<Integer> ids = new ArrayList<>();
            genericProjectList.forEach(project -> ids.add(project.getId()));
            if (!ids.isEmpty()) {
                // 删除项目下的目录
                categoryService.deleteServiceByIds(ids);
            }
        }
        int i = projectMapper.delete(queryWrapper);
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

    public String response(int result, String returnMessage) {
        if (result == 0) {
            throw new GenericException(ResponseStatusEnum.FAIL);
        } else {
            this.deleteCachedKeys();
        }
        return returnMessage;
    }

}