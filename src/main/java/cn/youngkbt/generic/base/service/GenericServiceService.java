package cn.youngkbt.generic.base.service;

import cn.youngkbt.generic.base.model.GenericService;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
public interface GenericServiceService {

	/**
	 * 根据 ID 查询一条数据
	 * @param conditionVos 查询条件
	 * @return 根据 ID 查询出的实体对象
	 */
	public List<GenericService> queryGenericServiceByConditions(List<ConditionVo> conditionVos);
	
	/**
	 * 查询所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<GenericService> queryGenericServiceList(GenericService genericService);
	
	/**
	 * 查询分页数据
	 * @return 所有数据的实体对象集合
	 */
	public IPage<GenericService> queryGenericServiceListPages(IPage<GenericService> page, GenericService genericService);

	/**
	 * 查询分页数据
	 * @param page 分页信息
	 * @return 所有数据的实体对象集合
	 */
	public IPage<GenericService> queryGenericServiceConditionsPages(IPage<GenericService> page, List<ConditionVo> conditionVos);
	
	

	/**
	 * 插入一条数据
	 * @param genericService 实体对象
	 * @return 插入的数据
	 */
	public GenericService insertGenericService(GenericService genericService);
	
	/**
	 * 更新一条数据
	 * @param genericService 实体对象
	 * @return 更新的数据
	 */
	public GenericService updateGenericService(GenericService genericService);
	
	/**
	 * 根据 ID 删除一条数据
	 * @param genericService 实体对象
	 * @return 删除的数据
	 */
	public GenericService deleteGenericServiceById(GenericService genericService);

	/**
	 * 根据 指定字段名 删除一条数据
	 * @param queryWrapper 字段名的集合
	 * @return 删除的行数
	 */
	public int deleteGenericServiceByColumns(QueryWrapper<GenericService> queryWrapper);

	/**
	 * 根据 ID集合 删除数据
	 * @param ids ID集合
	 * @return 删除的行数
	 */
	public int deleteGenericServiceByIds(List<Integer> ids);

}