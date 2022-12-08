package cn.youngkbt.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.youngkbt.base.model.GenericCategory;
import cn.youngkbt.base.service.GenericCategoryService;
import cn.youngkbt.base.service.GenericProjectService;
import cn.youngkbt.base.model.GenericProject;
import cn.youngkbt.base.mapper.GenericProjectMapper;
import cn.youngkbt.base.service.GenericServiceService;
import cn.youngkbt.exception.ConditionSqlException;
import cn.youngkbt.utils.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kele-Bingtang
 * @version 1.0
 * @since 2022-12-03 22:45:22
 */
@Service
public class GenericProjectServiceImpl implements GenericProjectService {

    @Autowired
    private GenericProjectMapper genericProjectMapper;
    @Autowired
    private GenericCategoryService genericCategoryService;
    @Autowired
    private GenericServiceService genericServiceService;

    @Override
    public List<GenericProject> queryGenericProjectByConditions(QueryWrapper<GenericProject> queryWrapper) {
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
        return genericProjectMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<GenericProject> queryGenericProjectListPages(IPage<GenericProject> page, GenericProject genericProject) {
        QueryWrapper<GenericProject> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(genericProject);
        try {
            return genericProjectMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    public IPage<GenericProject> queryGenericProjectConditionsPages(IPage<GenericProject> page, QueryWrapper<GenericProject> queryWrapper) {
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
        String noBarUUID = SecureUtil.noBarUUID();
        genericProject.setSecretKey(noBarUUID);
        int i = genericProjectMapper.insert(genericProject);
        // 生成默认目录
        GenericCategory category = new GenericCategory();
        category.setCategoryName("默认目录");
        category.setProjectId(genericProject.getId());
        category.setCreateUser(genericProject.getCreateUser());
        category.setModifyUser(genericProject.getModifyUser());
        genericCategoryService.insertGenericCategory(category);
        if (i == 0) {
            return null;
        }
        return genericProject;
    }

    @Override
    public GenericProject updateGenericProject(GenericProject genericProject) {
        int i = genericProjectMapper.updateById(genericProject);
        if (i == 0) {
            return null;
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
        }
        return genericProject;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteGenericProjectByColumns(QueryWrapper<GenericProject> queryWrapper) {
        queryWrapper.select("id");
        // 获取将要删除项目的主键
        List<GenericProject> genericProjectList = genericProjectMapper.selectList(queryWrapper);
        if(!genericProjectList.isEmpty()) {
            List<Integer> ids = new ArrayList<>();
            genericProjectList.forEach(project -> ids.add(project.getId()));
            if (!ids.isEmpty()) {
                // 删除项目下的目录
                genericCategoryService.deleteGenericServiceByIds(ids);
            }
        }
        return genericProjectMapper.delete(queryWrapper);
    }

}