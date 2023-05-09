package cn.youngkbt.generic.controller;

import cn.youngkbt.generic.common.dto.category.CategoryDeleteDTO;
import cn.youngkbt.generic.common.dto.category.CategoryInsertDTO;
import cn.youngkbt.generic.common.dto.category.CategoryQueryDTO;
import cn.youngkbt.generic.common.dto.category.CategoryUpdateDTO;
import cn.youngkbt.generic.common.http.HttpResult;
import cn.youngkbt.generic.common.http.Response;
import cn.youngkbt.generic.common.model.GenericCategory;
import cn.youngkbt.generic.service.CategoryService;
import cn.youngkbt.generic.vo.CategoryVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 目录相关接口
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 查询目录集合
     * @param categoryQueryDTO
     * @return
     */
    @GetMapping("/queryCategoryList")
    public Response<List<CategoryVO>> queryCategoryList(@Validated CategoryQueryDTO categoryQueryDTO) {
        List<GenericCategory> categoryList = categoryService.queryCategoryList(categoryQueryDTO);
        return HttpResult.ok(this.getCategoryVOList(categoryList));
    }

    /**
     * 查询目录集合，支持分页
     * @param categoryQueryDTO
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/queryCategoryListPages")
    public Response<List<CategoryVO>> queryCategoryListPages(@Validated CategoryQueryDTO categoryQueryDTO,
                                                             @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                                                             @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        IPage<GenericCategory> page = new Page<>(pageNo, pageSize);
        List<GenericCategory> categoryList = categoryService.queryCategoryListPages(page, categoryQueryDTO);
        return HttpResult.ok(this.getCategoryVOList(categoryList));
    }

    /**
     * 新增目录数据
     * @param categoryInsertDTO
     * @return
     */
    @PostMapping("/insertCategory")
    public Response<String> insertCategory(@Validated @RequestBody CategoryInsertDTO categoryInsertDTO) {
        String result = categoryService.insertCategory(categoryInsertDTO);
        return HttpResult.okMessage(result);
    }

    /**
     * 更新目录数据
     * @param categoryUpdateDTO
     * @return
     */
    @PostMapping("/updateCategory")
    public Response<String> updateCategory(@Validated @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        String result = categoryService.updateCategory(categoryUpdateDTO);
        return HttpResult.okMessage(result);
    }

    /**
     * 删除目录数据
     * @param categoryDeleteDTO
     * @return
     */
    @PostMapping("/deleteCategoryById")
    public Response<String> deleteCategoryById(@Validated @RequestBody CategoryDeleteDTO categoryDeleteDTO) {
        String result = categoryService.deleteCategoryById(categoryDeleteDTO);
        return HttpResult.okMessage(result);
    }

    /**
     * 封装 VO
     * @param categoryList
     * @return
     */
    public List<CategoryVO> getCategoryVOList(List<GenericCategory> categoryList) {
        List<CategoryVO> categoryVOList = new ArrayList<>();
        categoryList.forEach(category -> {
            CategoryVO vo = new CategoryVO();
            BeanUtils.copyProperties(category, vo);
            categoryVOList.add(vo);
        });
        return categoryVOList;
    }

}