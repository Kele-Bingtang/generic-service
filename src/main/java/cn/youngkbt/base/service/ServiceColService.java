package cn.youngkbt.base.service;

import java.util.List;

import cn.youngkbt.base.model.ServiceCol;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @author Kele-Bingtang
 * @since 2022-12-03 22:45:22
 * @version 1.0
 */
public interface ServiceColService {

	/**
	 * 根据 ID 查询一条数据
	 * @param queryWrapper 查询条件
	 * @return 根据 ID 查询出的实体对象
	 */
	public List<ServiceCol> queryServiceColByConditions(QueryWrapper<ServiceCol> queryWrapper);
	
	/**
	 * 查询所有数据
	 * @return 所有数据的实体对象集合
	 */
	public List<ServiceCol> queryServiceColList(ServiceCol serviceCol);
	
	/**
	 * 插入一条数据
	 * @param serviceCol 实体对象
	 * @return 插入的数据
	 */
	public ServiceCol insertServiceCol(ServiceCol serviceCol);
	
	/**
	 * 更新一条数据
	 *
	 * @param serviceCol 实体对象
	 * @return 更新的数据
	 */
	public ServiceCol updateServiceCol(ServiceCol serviceCol);
	
	/**
	 * 根据 ID 删除一条数据
	 * @param serviceCol 实体对象
	 * @return 删除的数据
	 */
	public ServiceCol deleteServiceColById(ServiceCol serviceCol);
	
	/**
	 * 根据 指定字段名 删除一条数据
	 * @param queryWrapper 查询条件
	 * @return 删除的行数
	 */
	public int deleteServiceColByColumns(QueryWrapper<ServiceCol> queryWrapper);

	/**
	 * 根据 ID集合 删除数据
	 * @param ids ID集合
	 * @return 删除的行数
	 */
	public int deleteServiceColByIds(List<Integer> ids);
	
}