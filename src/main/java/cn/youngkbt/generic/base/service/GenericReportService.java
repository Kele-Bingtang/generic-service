package cn.youngkbt.generic.base.service;

import cn.youngkbt.generic.base.model.GenericReport;
import cn.youngkbt.generic.vo.ConditionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author Kele-Bingtang
 * @date 2022-12-03 22:45:22
 * @note 1.0
 */
public interface GenericReportService {

	/**
	 * 根据 ID 查询一条数据
	 * @param conditionVos 查询条件
	 * @return 根据 ID 查询出的实体对象
	 */
	public List<GenericReport>  queryGenericReportByCondition(List<ConditionVo> conditionVos);
	
	/**
	 * 查询所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<GenericReport> queryGenericReportList(GenericReport genericReport);

	public GenericReport queryOneGenericReport(GenericReport genericReport);

	/**
	 * 查询分页数据
	 * @param page 分页信息
	 * @return 所有数据的实体对象集合
	 */
	public IPage<GenericReport> queryGenericReportPages(IPage<GenericReport> page, QueryWrapper<GenericReport> queryWrapper);
	
	/**
	 * 插入一条数据
	 * @param genericReport 实体对象
	 * @return 插入的数据
	 */
	public GenericReport insertGenericReport(GenericReport genericReport);
	
	/**
	 * 更新一条数据
	 * @param genericReport 实体对象
	 * @return 更新的数据
	 */
	public GenericReport updateGenericReport(GenericReport genericReport);
	
	/**
	 * 根据 ID 删除一条数据
	 * @param genericReport 实体对象
	 * @return 删除的数据
	 */
	public GenericReport deleteGenericReportById(GenericReport genericReport);

	/**
	 * 根据 指定字段名 删除一条数据
	 * @param queryWrapper 字段名的集合
	 * @return 删除的行数
	 */
	public int deleteGenericReportByColumns(QueryWrapper<GenericReport> queryWrapper);
	
	/**
	 * 根据 ID集合 删除数据
	 * @param ids ID集合
	 * @return 删除的行数
	 */
	public int deleteGenericReportByIds(List<Integer> ids);
	
}