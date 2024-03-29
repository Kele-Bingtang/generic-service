package cn.youngkbt.generic.service.impl;

import cn.youngkbt.generic.common.dto.category.CategoryDeleteDTO;
import cn.youngkbt.generic.common.dto.category.CategoryInsertDTO;
import cn.youngkbt.generic.common.dto.category.CategoryQueryDTO;
import cn.youngkbt.generic.common.dto.category.CategoryUpdateDTO;
import cn.youngkbt.generic.common.exception.GenericException;
import cn.youngkbt.generic.common.http.ResponseStatusEnum;
import cn.youngkbt.generic.common.model.GenericCategory;
import cn.youngkbt.generic.common.model.GenericService;
import cn.youngkbt.generic.common.utils.ObjectUtils;
import cn.youngkbt.generic.security.SecurityUtils;
import cn.youngkbt.generic.mapper.CategoryMapper;
import cn.youngkbt.generic.service.CategoryService;
import cn.youngkbt.generic.service.ServiceService;
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
 * @date 2022-12-03 22:45:22
 * @note
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ServiceService serviceService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<GenericCategory> queryCategoryList(CategoryQueryDTO categoryQueryDTO) {
        GenericCategory genericCategory = new GenericCategory();
        BeanUtils.copyProperties(categoryQueryDTO, genericCategory);
        // String key = SecurityUtils.getUsername() + "_category_" + genericCategory.toString();
        // 从 Redis 拿缓存
        // Object o = redisTemplate.opsForValue().get(key);
        // if (o instanceof List) {
        //     List<CategoryVO> categoryPage = (List<CategoryVO>) o;
        //     LOGGER.info("从 Redis 拿到数据：{}", categoryPage);
        //     return categoryPage;
        // }
        QueryWrapper<GenericCategory> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(genericCategory);
        List<GenericCategory> categories = categoryMapper.selectList(queryWrapper);
        // 存入 Redis
        // redisTemplate.opsForValue().set(key, categoryVOList, 24, TimeUnit.HOURS);
        return categories;
    }

    @Override
    public List<GenericCategory> queryCategoryListPages(IPage<GenericCategory> page, CategoryQueryDTO categoryQueryDTO) {
        GenericCategory genericCategory = new GenericCategory();
        BeanUtils.copyProperties(categoryQueryDTO, genericCategory);
        // String key = SecurityUtils.getUsername() + "_category_" + page.getCurrent() + "_" + page.getSize() + "_" + genericCategory.toString();
        // 从 Redis 拿缓存
        // Object o = redisTemplate.opsForValue().get(key);
        // if (o instanceof List) {
        //     List<CategoryVO> categoryPage = (List<CategoryVO>) o;
        //     LOGGER.info("从 Redis 拿到数据：{}", categoryPage);
        //     return categoryPage;
        // }
        QueryWrapper<GenericCategory> queryWrapper = new QueryWrapper<>();
        // 如果 genericCategory 没有数据，则返回全部数据
        queryWrapper.setEntity(genericCategory);
        try {
            IPage<GenericCategory> genericCategoryPage = categoryMapper.selectPage(page, queryWrapper);
            List<GenericCategory> records = genericCategoryPage.getRecords();
            // 存入 Redis
            // redisTemplate.opsForValue().set(key, records, 24, TimeUnit.HOURS);
            return records;
        } catch (Exception e) {
            throw new GenericException(ResponseStatusEnum.CONDITION_SQL_ERROR);
        }
    }

    @Override
    public String insertCategory(CategoryInsertDTO categoryInsertDTO) {
        // 先判断是否存在目录
        QueryWrapper<GenericCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_code", categoryInsertDTO.getCategoryCode()).eq("project_id", categoryInsertDTO.getProjectId());
        GenericCategory genericCategory = categoryMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(genericCategory) && genericCategory.getId() > 0) {
            throw new GenericException(ResponseStatusEnum.CATEGORY_EXEIT);
        }
        GenericCategory category = new GenericCategory();
        BeanUtils.copyProperties(categoryInsertDTO, category);
        int result = categoryMapper.insert(category);
        return this.response(result, "插入成功");
    }

    @Override
    public String updateCategory(CategoryUpdateDTO categoryUpdateDTO) {
        GenericCategory genericCategory = new GenericCategory();
        BeanUtils.copyProperties(categoryUpdateDTO, genericCategory);
        int result = categoryMapper.updateById(genericCategory);
        return this.response(result, "更新成功");

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteCategoryById(CategoryDeleteDTO categoryDeleteDTO) {
        GenericCategory genericCategory = new GenericCategory();
        BeanUtils.copyProperties(categoryDeleteDTO, genericCategory);
        // 删除目录下的接口
        QueryWrapper<GenericService> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", genericCategory.getId());
        serviceService.deleteServiceByColumns(queryWrapper);
        int result = categoryMapper.deleteById(genericCategory);
        return this.response(result, "更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteServiceByColumns(QueryWrapper<GenericCategory> queryWrapper) {
        queryWrapper.select("id");
        // 获取即将删除目录的主键
        List<GenericCategory> categoryList = categoryMapper.selectList(queryWrapper);
        if (!categoryList.isEmpty()) {
            List<Integer> ids = new ArrayList<>();
            categoryList.forEach(category -> ids.add(category.getId()));
            // 根据目录的主键删除接口
            if (!ids.isEmpty()) {
                serviceService.deleteServiceByIds(ids);
            }
        }
        int i = categoryMapper.delete(queryWrapper);
        if (i > 0) {
            this.deleteCachedKeys();
        }
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteServiceByIds(List<Integer> ids) {
        // 根据目录的主键删除接口
        serviceService.deleteServiceByIds(ids);
        int i = categoryMapper.deleteBatchIds(ids);
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