package cn.youngkbt.base.controller;

import java.util.List;

import cn.youngkbt.base.model.GenericCategory;
import cn.youngkbt.base.service.impl.GenericCategoryServiceImpl;
import cn.youngkbt.http.Response;
import cn.youngkbt.http.HttpResult;
import cn.youngkbt.utils.SearchUtil;
import cn.youngkbt.valid.ValidList;
import cn.youngkbt.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Kele-Bingtang
 * @version 1.0
 * @since 2022-12-03 22:45:22
 */
@RestController
@RequestMapping("/genericCategory")
public class GenericCategoryController {

    @Autowired
    private GenericCategoryServiceImpl genericCategoryService;

    @GetMapping("/queryGenericCategoryByConditions")
    public Response queryGenericCategoryByConditions(@RequestBody List<ConditionVo> conditionVos) {
        QueryWrapper<GenericCategory> queryWrapper = SearchUtil.parseWhereSql(conditionVos, GenericCategory.class);
        List<GenericCategory> category = genericCategoryService.queryGenericCategoryByCondition(queryWrapper);
        return HttpResult.ok(category);
    }

    @GetMapping("/queryGenericCategoryList")
    public Response queryGenericCategoryList(GenericCategory genericCategory) {
        List<GenericCategory> categoryList = genericCategoryService.queryGenericCategoryList(genericCategory);
        return HttpResult.ok(categoryList);
    }

    @GetMapping("/queryGenericCategoryListPages")
    public Response queryGenericCategoryListPages(GenericCategory genericCategory, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericCategory> page = new Page<>(pageNo, pageSize);
        IPage<GenericCategory> categoryList = genericCategoryService.queryGenericCategoryListPages(page, genericCategory);
        return HttpResult.ok(categoryList);
    }

    @GetMapping("/queryGenericCategoryConditionsPages")
    public Response queryGenericCategoryConditionsPages(@Validated @RequestBody(required = false) ValidList<ConditionVo> conditionVos, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericCategory> page = new Page<>(pageNo, pageSize);
        QueryWrapper<GenericCategory> queryWrapper = SearchUtil.parseWhereSql(conditionVos, GenericCategory.class);
        IPage<GenericCategory> categoryList = genericCategoryService.queryGenericCategoryConditionsPages(page, queryWrapper);
        return HttpResult.ok(categoryList);
    }

    @PostMapping("/insertGenericCategory")
    public Response insertGenericCategory(@Validated(GenericCategory.CategoryInsert.class) @RequestBody GenericCategory genericCategory) {
        GenericCategory category = genericCategoryService.insertGenericCategory(genericCategory);
        return HttpResult.okOrFail(category);
    }

    @PostMapping("/updateGenericCategory")
    public Response updateGenericCategory(@Validated(GenericCategory.CategoryUpdate.class) @RequestBody GenericCategory genericCategory) {
        GenericCategory category = genericCategoryService.updateGenericCategory(genericCategory);
        return HttpResult.okOrFail(category);
    }

    @PostMapping("/deleteGenericCategoryById")
    public Response deleteGenericCategoryById(@Validated(GenericCategory.CategoryDelete.class) @RequestBody GenericCategory genericCategory) {
        GenericCategory category = genericCategoryService.deleteGenericCategoryById(genericCategory);
        return HttpResult.okOrFail(category);
    }

}