package cn.youngkbt.generic.base.service.impl;

import cn.youngkbt.generic.base.mapper.GenericCategoryMapper;
import cn.youngkbt.generic.base.model.GenericCategory;
import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.base.service.GenericCategoryService;
import cn.youngkbt.generic.base.service.GenericServiceService;
import cn.youngkbt.generic.exception.ExecuteSqlException;
import cn.youngkbt.generic.utils.SearchUtils;
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
public class GenericCategoryServiceImpl implements GenericCategoryService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GenericCategoryMapper genericCategoryMapper;

    @Resource
    private GenericServiceService genericServiceService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<GenericCategory> queryGenericCategoryByCondition(List<ConditionVo> conditionVos) {
        QueryWrapper<GenericCategory> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericCategory.class);
        try {
            return genericCategoryMapper.selectList(queryWrapper);
        } catch (Exception e) {
            throw new ExecuteSqlException();
        }
    }

    @Override
    public List<GenericCategory> queryGenericCategoryList(GenericCategory genericCategory) {
        String key = SecurityUtils.getUsername() + "_category_" + genericCategory.toString();
        // ??? Redis ?????????
        Object o = redisTemplate.opsForValue().get(key);
        if (o instanceof List) {
            List<GenericCategory> categoryPage = (List<GenericCategory>) o;
            LOGGER.info("??? Redis ???????????????{}", categoryPage);
            return categoryPage;
        }
        QueryWrapper<GenericCategory> queryWrapper = new QueryWrapper<>();
        // ?????? genericCategory ????????????????????????????????????
        queryWrapper.setEntity(genericCategory);
        List<GenericCategory> categories = genericCategoryMapper.selectList(queryWrapper);
        // ?????? Redis
        redisTemplate.opsForValue().set(key, categories, 24, TimeUnit.HOURS);
        return categories;
    }

    @Override
    public IPage<GenericCategory> queryGenericCategoryListPages(IPage<GenericCategory> page, GenericCategory genericCategory) {
        String key = SecurityUtils.getUsername() + "_category_" + page.getCurrent() + "_" + page.getSize() + "_" + genericCategory.toString();
        // ??? Redis ?????????
        Object o = redisTemplate.opsForValue().get(key);
        if (o instanceof IPage) {
            IPage<GenericCategory> categoryPage = (IPage<GenericCategory>) o;
            LOGGER.info("??? Redis ???????????????{}", categoryPage.getRecords());
            return categoryPage;
        }
        QueryWrapper<GenericCategory> queryWrapper = new QueryWrapper<>();
        // ?????? genericCategory ????????????????????????????????????
        queryWrapper.setEntity(genericCategory);
        try {
            IPage<GenericCategory> genericCategoryPage = genericCategoryMapper.selectPage(page, queryWrapper);
            // ?????? Redis
            redisTemplate.opsForValue().set(key, genericCategoryPage, 24, TimeUnit.HOURS);
            return genericCategoryPage;
        } catch (Exception e) {
            throw new ExecuteSqlException();
        }
    }

    @Override
    public IPage<GenericCategory> queryGenericCategoryConditionsPages(IPage<GenericCategory> page, List<ConditionVo> conditionVos) {
        QueryWrapper<GenericCategory> queryWrapper = SearchUtils.parseWhereSql(conditionVos, GenericCategory.class);
        try {
            return genericCategoryMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            throw new ExecuteSqlException();
        }
    }

    @Override
    public GenericCategory insertGenericCategory(GenericCategory genericCategory) {
        int i = genericCategoryMapper.insert(genericCategory);
        if (i == 0) {
            return null;
        } else {
            this.deleteCachedKeys();
        }
        return genericCategory;
    }

    @Override
    public GenericCategory updateGenericCategory(GenericCategory genericCategory) {
        int i = genericCategoryMapper.updateById(genericCategory);
        if (i == 0) {
            return null;
        } else {
            this.deleteCachedKeys();
        }
        return genericCategory;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GenericCategory deleteGenericCategoryById(GenericCategory genericCategory) {
        // ????????????????????????
        QueryWrapper<GenericService> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", genericCategory.getId());
        genericServiceService.deleteGenericServiceByColumns(queryWrapper);
        int i = genericCategoryMapper.deleteById(genericCategory);
        if (i == 0) {
            return null;
        } else {
            this.deleteCachedKeys();
        }
        return genericCategory;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteGenericServiceByColumns(QueryWrapper<GenericCategory> queryWrapper) {
        queryWrapper.select("id");
        // ?????????????????????????????????
        List<GenericCategory> categoryList = genericCategoryMapper.selectList(queryWrapper);
        if (!categoryList.isEmpty()) {
            List<Integer> ids = new ArrayList<>();
            categoryList.forEach(category -> ids.add(category.getId()));
            // ?????????????????????????????????
            if (!ids.isEmpty()) {
                genericServiceService.deleteGenericServiceByIds(ids);
            }
        }
        int i = genericCategoryMapper.delete(queryWrapper);
        if (i > 0) {
            this.deleteCachedKeys();
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteGenericServiceByIds(List<Integer> ids) {
        // ?????????????????????????????????
        genericServiceService.deleteGenericServiceByIds(ids);
        int i = genericCategoryMapper.deleteBatchIds(ids);
        if (i > 0) {
            this.deleteCachedKeys();
        }
        return i;
    }

    public void deleteCachedKeys() {
        Set<String> keys = redisTemplate.keys(SecurityUtils.getUsername() + "_category*");
        if (null != keys) {
            Long delete = redisTemplate.delete(keys);
            if (null != delete) {
                LOGGER.info("????????? {} ??? key", delete);
            }
        }
    }

}