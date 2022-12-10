package cn.youngkbt.generic.base.service;

import java.util.List;

import cn.youngkbt.generic.base.model.GenericCategory;
import cn.youngkbt.generic.exception.ConditionSqlException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author Kele-Bingtang
 * @since 2022-12-03 22:45:22
 * @version 1.0
 */
public interface GenericCategoryService {

	/**
	 * 根据 ID 查询一条数据
	 * @param queryWrapper 查询条件
	 * @return 根据 ID 查询出的实体对象
	 */
	public List<GenericCategory> queryGenericCategoryByCondition(QueryWrapper<GenericCategory> queryWrapper) throws ConditionSqlException;
	
	/**
	 * 查询所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<GenericCategory> queryGenericCategoryList(GenericCategory genericCategory);
	
	/**
	 * 查询分页数据
	 * @return 所有数据的实体对象集合
	 */
	public IPage<GenericCategory> queryGenericCategoryListPages(IPage<GenericCategory> page, GenericCategory genericCategory);

	/**
	 * 查询分页数据
	 * @return 所有数据的实体对象集合
	 */
	public IPage<GenericCategory> queryGenericCategoryConditionsPages(IPage<GenericCategory> page, QueryWrapper<GenericCategory> queryWrapper);
	
	/**
	 * 插入一条数据
	 * @param genericCategory 实体对象
	 * @return 插入的数据
	 */
	public GenericCategory insertGenericCategory(GenericCategory genericCategory);
	
	/**
	 * 更新一条数据
	 * @param genericCategory 实体对象
	 * @return 更新的数据
	 */
	public GenericCategory updateGenericCategory(GenericCategory genericCategory);
	
	/**
	 * 根据 ID 删除一条数据
	 * @param genericCategory 实体对象
	 * @return 删除的数据
	 */
	public GenericCategory deleteGenericCategoryById(GenericCategory genericCategory);
	
	/**
	 * 根据 指定字段名 删除一条数据
	 * @param queryWrapper 字段名的集合
	 * @return 删除的行数
	 */
	public int deleteGenericServiceByColumns(QueryWrapper<GenericCategory> queryWrapper);

	/**
	 * 根据 ID集合 删除数据
	 * @param ids ID集合
	 * @return 删除的行数
	 */
	public int deleteGenericServiceByIds(List<Integer> ids);

}