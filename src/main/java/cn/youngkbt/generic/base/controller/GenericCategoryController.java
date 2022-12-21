package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.model.GenericCategory;
import cn.youngkbt.generic.base.service.GenericCategoryService;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.valid.ValidList;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @note 1.0
 * @date 2022-12-03 22:45:22
 */
@RestController
@RequestMapping("/genericCategory")
public class GenericCategoryController {

    @Autowired
    private GenericCategoryService genericCategoryService;

    @GetMapping("/queryGenericCategoryByConditions")
    public Response queryGenericCategoryByConditions(@Validated @RequestBody ValidList<ConditionVo> conditionVos) {
        List<GenericCategory> category = genericCategoryService.queryGenericCategoryByCondition(conditionVos);
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
        return HttpResult.ok(categoryList.getRecords());
    }

    @GetMapping("/queryGenericCategoryConditionsPages")
    public Response queryGenericCategoryConditionsPages(@Validated @RequestBody(required = false) ValidList<ConditionVo> conditionVos, @RequestParam(defaultValue = "1", required = false) Integer pageNo, @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericCategory> page = new Page<>(pageNo, pageSize);
        IPage<GenericCategory> categoryList = genericCategoryService.queryGenericCategoryConditionsPages(page, conditionVos);
        return HttpResult.ok(categoryList.getRecords());
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