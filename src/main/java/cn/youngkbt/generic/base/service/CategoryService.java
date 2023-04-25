package cn.youngkbt.generic.base.service;

import cn.youngkbt.generic.base.dto.category.CategoryDeleteDTO;
import cn.youngkbt.generic.base.dto.category.CategoryInsertDTO;
import cn.youngkbt.generic.base.dto.category.CategoryQueryDTO;
import cn.youngkbt.generic.base.dto.category.CategoryUpdateDTO;
import cn.youngkbt.generic.base.model.GenericCategory;
import cn.youngkbt.generic.base.vo.CategoryVO;
import cn.youngkbt.generic.exception.GenericException;
import cn.youngkbt.generic.base.dto.ConditionDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
public interface CategoryService {

	/**
	 * 根据 ID 查询一条数据
	 * @param conditionDTOList 查询条件
	 * @return 根据 ID 查询出的实体对象
	 */
	public List<CategoryVO> queryCategoryByCondition(List<ConditionDTO> conditionDTOList) throws GenericException;
	
	/**
	 * 查询所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<CategoryVO> queryCategoryList(CategoryQueryDTO categoryQueryDTO);
	
	/**
	 * 查询分页数据
	 * @return 所有数据的实体对象集合
	 */
	public List<CategoryVO> queryCategoryListPages(IPage<GenericCategory> page, CategoryQueryDTO categoryQueryDTO);

	/**
	 * 插入一条数据
	 * @param categoryInsertDTO 实体对象
	 * @return 插入的数据
	 */
	public String insertCategory(CategoryInsertDTO categoryInsertDTO);
	
	/**
	 * 更新一条数据
	 * @param categoryUpdateDTO 实体对象
	 * @return 更新的数据
	 */
	public String updateCategory(CategoryUpdateDTO categoryUpdateDTO);
	
	/**
	 * 根据 ID 删除一条数据
	 * @param categoryDeleteDTO 实体对象
	 * @return 删除的数据
	 */
	public String deleteCategoryById(CategoryDeleteDTO categoryDeleteDTO);
	
	/**
	 * 根据 指定字段名 删除一条数据
	 * @param queryWrapper 字段名的集合
	 * @return 删除的行数
	 */
	public int deleteServiceByColumns(QueryWrapper<GenericCategory> queryWrapper);

	/**
	 * 根据 ID集合 删除数据
	 * @param ids ID集合
	 * @return 删除的行数
	 */
	public int deleteServiceByIds(List<Integer> ids);

}