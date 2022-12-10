package cn.youngkbt.generic.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.service.GenericCategoryService;
import cn.youngkbt.generic.base.model.GenericCategory;
import cn.youngkbt.generic.base.mapper.GenericCategoryMapper;
import cn.youngkbt.generic.base.service.GenericServiceService;
import cn.youngkbt.generic.exception.ConditionSqlException;
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
public class GenericCategoryServiceImpl implements GenericCategoryService {

    @Autowired
    private GenericCategoryMapper genericCategoryMapper;

    @Autowired
    private GenericServiceService genericServiceService;

    @Override
    public List<GenericCategory> queryGenericCategoryByCondition(QueryWrapper<GenericCategory> queryWrapper) {
        try {
            return genericCategoryMapper.selectList(queryWrapper);
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    public List<GenericCategory> queryGenericCategoryList(GenericCategory genericCategory) {
        QueryWrapper<GenericCategory> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(genericCategory);
        return genericCategoryMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<GenericCategory> queryGenericCategoryListPages(IPage<GenericCategory> page, GenericCategory genericCategory) {
        QueryWrapper<GenericCategory> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(genericCategory);
        try {
            return genericCategoryMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    public IPage<GenericCategory> queryGenericCategoryConditionsPages(IPage<GenericCategory> page, QueryWrapper<GenericCategory> queryWrapper) {
        try {
            return genericCategoryMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            throw new ConditionSqlException();
        }
    }

    @Override
    public GenericCategory insertGenericCategory(GenericCategory genericCategory) {
        int i = genericCategoryMapper.insert(genericCategory);
        if (i == 0) {
            return null;
        }
        return genericCategory;
    }

    @Override
    public GenericCategory updateGenericCategory(GenericCategory genericCategory) {
        int i = genericCategoryMapper.updateById(genericCategory);
        if (i == 0) {
            return null;
        }
        return genericCategory;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenericCategory deleteGenericCategoryById(GenericCategory genericCategory) {
        // 删除目录下的接口
        QueryWrapper<GenericService> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", genericCategory.getId());
        genericServiceService.deleteGenericServiceByColumns(queryWrapper);
        int i = genericCategoryMapper.deleteById(genericCategory);
        if (i == 0) {
            return null;
        }
        return genericCategory;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteGenericServiceByColumns(QueryWrapper<GenericCategory> queryWrapper) {
        queryWrapper.select("id");
        // 获取即将删除目录的主键
        List<GenericCategory> categoryList = genericCategoryMapper.selectList(queryWrapper);
        if (!categoryList.isEmpty()) {
            List<Integer> ids = new ArrayList<>();
            categoryList.forEach(category -> ids.add(category.getId()));
            // 根据目录的主键删除接口
            if (!ids.isEmpty()) {
                genericServiceService.deleteGenericServiceByIds(ids);
            }
        }
        return genericCategoryMapper.delete(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteGenericServiceByIds(List<Integer> ids) {
        // 根据目录的主键删除接口
        genericServiceService.deleteGenericServiceByIds(ids);
        return genericCategoryMapper.deleteBatchIds(ids);
    }

}