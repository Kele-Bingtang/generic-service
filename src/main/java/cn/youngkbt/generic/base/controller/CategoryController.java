package cn.youngkbt.generic.base.controller;

import cn.youngkbt.generic.base.dto.ConditionDTO;
import cn.youngkbt.generic.base.dto.category.CategoryDeleteDTO;
import cn.youngkbt.generic.base.dto.category.CategoryInsertDTO;
import cn.youngkbt.generic.base.dto.category.CategoryQueryDTO;
import cn.youngkbt.generic.base.dto.category.CategoryUpdateDTO;
import cn.youngkbt.generic.base.model.GenericCategory;
import cn.youngkbt.generic.base.service.CategoryService;
import cn.youngkbt.generic.base.vo.CategoryVO;
import cn.youngkbt.generic.http.HttpResult;
import cn.youngkbt.generic.http.Response;
import cn.youngkbt.generic.valid.ValidList;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @note 1.0
 * @date 2022-12-03 22:45:22
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/queryCategoryByConditions")
    public Response<List<CategoryVO>> queryCategoryByConditions(@Validated @RequestBody ValidList<ConditionDTO> conditionDTOList) {
        List<CategoryVO> categoryVOList = categoryService.queryCategoryByCondition(conditionDTOList);
        return HttpResult.ok(categoryVOList);
    }

    @GetMapping("/queryCategoryList")
    public Response<List<CategoryVO>> queryCategoryList(@Validated CategoryQueryDTO categoryQueryDTO) {
        List<CategoryVO> categoryVOList = categoryService.queryCategoryList(categoryQueryDTO);
        return HttpResult.ok(categoryVOList);
    }

    @GetMapping("/queryCategoryListPages")
    public Response<List<CategoryVO>> queryCategoryListPages(@Validated CategoryQueryDTO categoryQueryDTO,
                                                             @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                             @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericCategory> page = new Page<>(pageNo, pageSize);
        List<CategoryVO> categoryVOList = categoryService.queryCategoryListPages(page, categoryQueryDTO);
        return HttpResult.ok(categoryVOList);
    }

    @PostMapping("/insertCategory")
    public Response<String> insertCategory(@Validated @RequestBody CategoryInsertDTO categoryInsertDTO) {
        String result = categoryService.insertCategory(categoryInsertDTO);
        return HttpResult.okMessage(result);
    }

    @PostMapping("/updateCategory")
    public Response<String> updateCategory(@Validated @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        String result = categoryService.updateCategory(categoryUpdateDTO);
        return HttpResult.okMessage(result);
    }

    @PostMapping("/deleteCategoryById")
    public Response<String> deleteCategoryById(@Validated @RequestBody CategoryDeleteDTO categoryDeleteDTO) {
        String result = categoryService.deleteCategoryById(categoryDeleteDTO);
        return HttpResult.okMessage(result);
    }

}